package io.github.jwyoon1220.stellaengine;

import io.github.jwyoon1220.stellaengine.entity.Model;
import io.github.jwyoon1220.stellaengine.entity.Texture;
import io.github.jwyoon1220.stellaengine.utils.Utils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.assimp.*;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    // --- OBJ 로더 ---
    public Model loadOBJModel(String fileName) {
        List<String> lines = Utils.INSTANCE.readAllLines(fileName);
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> texturesList = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Vector3i> faces = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.trim().split("\\s+");
            if (tokens.length < 2) continue;
            String type = tokens[0];
            switch (type) {
                case "v" -> vertices.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
                case "vt" -> texturesList.add(new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
                case "vn" -> normals.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
                case "f" -> {
                    for (int i = 2; i < tokens.length - 1; i++) {
                        processFace(tokens[1], faces);
                        processFace(tokens[i], faces);
                        processFace(tokens[i + 1], faces);
                    }
                }
            }
        }

        List<Float> finalVertices = new ArrayList<>();
        List<Float> finalTexCoords = new ArrayList<>();
        List<Float> finalNormals = new ArrayList<>();
        List<Integer> finalIndices = new ArrayList<>();
        int currentIndex = 0;
        for (Vector3i face : faces) {
            Vector3f v = vertices.get(face.x);
            finalVertices.add(v.x); finalVertices.add(v.y); finalVertices.add(v.z);
            if (face.y >= 0 && !texturesList.isEmpty()) {
                Vector2f tex = texturesList.get(face.y);
                finalTexCoords.add(tex.x); finalTexCoords.add(1.0f - tex.y);
            } else {
                finalTexCoords.add(0.0f); finalTexCoords.add(0.0f);
            }
            if (face.z >= 0 && !normals.isEmpty()) {
                Vector3f n = normals.get(face.z);
                finalNormals.add(n.x); finalNormals.add(n.y); finalNormals.add(n.z);
            } else {
                finalNormals.add(0.0f); finalNormals.add(1.0f); finalNormals.add(0.0f);
            }
            finalIndices.add(currentIndex++);
        }
        return loadModel(listToFloatArray(finalVertices), listToFloatArray(finalTexCoords), listToFloatArray(finalNormals), finalIndices.stream().mapToInt(i -> i).toArray());
    }

    private void processFace(String token, List<Vector3i> faces) {
        String[] parts = token.split("/");
        int pos = Integer.parseInt(parts[0]) - 1;
        int tex = (parts.length > 1 && !parts[1].isEmpty()) ? Integer.parseInt(parts[1]) - 1 : -1;
        int norm = (parts.length > 2 && !parts[2].isEmpty()) ? Integer.parseInt(parts[2]) - 1 : -1;
        faces.add(new Vector3i(pos, tex, norm));
    }

    // --- GLB 로더 (강화됨) ---
    public Model loadGLBModel(String fileName) {
        AIScene scene = Assimp.aiImportFile(fileName,
                Assimp.aiProcess_Triangulate | Assimp.aiProcess_GenSmoothNormals |
                        Assimp.aiProcess_FlipUVs | Assimp.aiProcess_JoinIdenticalVertices);

        if (scene == null || scene.mRootNode() == null) {
            throw new RuntimeException("GLB 로드 실패: " + Assimp.aiGetErrorString());
        }

        AIMesh mesh = AIMesh.create(scene.mMeshes().get(0));
        int vertexCount = mesh.mNumVertices();

        float[] vertices = new float[vertexCount * 3];
        float[] normals = new float[vertexCount * 3];
        float[] texCoords = new float[vertexCount * 2];

        for (int i = 0; i < vertexCount; i++) {
            AIVector3D v = mesh.mVertices().get(i);
            vertices[i * 3] = v.x(); vertices[i * 3 + 1] = v.y(); vertices[i * 3 + 2] = v.z();
            if (mesh.mNormals() != null) {
                AIVector3D n = mesh.mNormals().get(i);
                normals[i * 3] = n.x(); normals[i * 3 + 1] = n.y(); normals[i * 3 + 2] = n.z();
            }
            if (mesh.mTextureCoords(0) != null) {
                AIVector3D tc = mesh.mTextureCoords(0).get(i);
                texCoords[i * 2] = tc.x(); texCoords[i * 2 + 1] = tc.y();
            }
        }

        int[] indices = new int[mesh.mNumFaces() * 3];
        for (int i = 0; i < mesh.mNumFaces(); i++) {
            AIFace face = mesh.mFaces().get(i);
            indices[i * 3] = face.mIndices().get(0);
            indices[i * 3 + 1] = face.mIndices().get(1);
            indices[i * 3 + 2] = face.mIndices().get(2);
        }

        Model model = loadModel(vertices, texCoords, normals, indices);

        // 머티리얼 처리 (더 넓은 범위 탐색)
        processGLBMaterial(scene, mesh, model);

        Assimp.aiReleaseImport(scene);
        return model;
    }

    private void processGLBMaterial(AIScene scene, AIMesh mesh, Model model) {
        int materialIdx = mesh.mMaterialIndex();
        if (materialIdx < 0) return;

        AIMaterial material = AIMaterial.create(scene.mMaterials().get(materialIdx));
        AIString path = AIString.calloc();

        // DIFFUSE(0번)뿐만 아니라 BASE_COLOR(PBR)까지 확인
        int textureId = 0;
        if (Assimp.aiGetMaterialTexture(material, Assimp.aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null) == Assimp.aiReturn_SUCCESS ||
                Assimp.aiGetMaterialTexture(material, Assimp.aiTextureType_BASE_COLOR, 0, path, (IntBuffer) null, null, null, null, null, null) == Assimp.aiReturn_SUCCESS) {

            String texturePath = path.dataString();
            if (!texturePath.isEmpty()) {
                if (texturePath.startsWith("*")) { // 임베디드
                    int idx = Integer.parseInt(texturePath.substring(1));
                    textureId = loadEmbeddedTexture(AITexture.create(scene.mTextures().get(idx)));
                } else {
                    textureId = loadTexture("texture/" + texturePath);
                }
            }
        }

        if (textureId > 0) {
            model.setTexture(new Texture(textureId));
            // 중요: 머티리얼의 텍스처 존재 여부를 셰이더가 알 수 있게 필드를 업데이트해야 할 수도 있습니다.
        }
        path.free();
    }

    private int loadEmbeddedTexture(AITexture aiTexture) {
        ByteBuffer imageBuffer;
        int width, height;

        // GLB의 경우 텍스처 좌표계가 뒤집혀있을 수 있으므로 STB 설정 확인
        STBImage.stbi_set_flip_vertically_on_load(false);

        if (aiTexture.mHeight() == 0) { // 압축 데이터
            ByteBuffer compressed = MemoryUtil.memByteBuffer(aiTexture.pcData().address(), aiTexture.mWidth());
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer w = stack.mallocInt(1); IntBuffer h = stack.mallocInt(1); IntBuffer c = stack.mallocInt(1);
                imageBuffer = STBImage.stbi_load_from_memory(compressed, w, h, c, 4);
                if (imageBuffer == null) return 0;
                width = w.get(0); height = h.get(0);
            }
        } else { // 비압축
            width = aiTexture.mWidth(); height = aiTexture.mHeight();
            imageBuffer = MemoryUtil.memByteBuffer(aiTexture.pcData().address(), width * height * 4);
        }

        int id = generateTexture(width, height, imageBuffer);
        if (aiTexture.mHeight() == 0) STBImage.stbi_image_free(imageBuffer);
        return id;
    }

    private int generateTexture(int width, int height, ByteBuffer buffer) {
        int id = GL11.glGenTextures();
        textures.add(id);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        // 텍스처 파라미터 강제 설정
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        return id;
    }

    // --- 공통 메서드 ---
    public Model loadModel(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
        int id = GL30.glGenVertexArrays(); vaos.add(id);
        GL30.glBindVertexArray(id);
        storeIndicesBuffer(indices);
        storeDataInAttribList(0, 3, vertices);
        storeDataInAttribList(1, 2, textureCoords);
        storeDataInAttribList(2, 3, normals);
        GL30.glBindVertexArray(0);
        return new Model(id, indices.length);
    }

    public int loadTexture(String fileName) {
        STBImage.stbi_set_flip_vertically_on_load(true); // 외부 파일은 보통 뒤집어 읽어야 함
        int width, height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1); IntBuffer h = stack.mallocInt(1); IntBuffer c = stack.mallocInt(1);
            buffer = STBImage.stbi_load(fileName, w, h, c, 4);
            if (buffer == null) return 0;
            width = w.get(); height = h.get();
        }
        return generateTexture(width, height, buffer);
    }

    private void storeIndicesBuffer(int[] indices) {
        int vbo = GL15.glGenBuffers(); vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Utils.INSTANCE.storeDataInIntBuffer(indices), GL15.GL_STATIC_DRAW);
    }

    private void storeDataInAttribList(int attribNo, int size, float[] data) {
        int vbo = GL15.glGenBuffers(); vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Utils.INSTANCE.storeDataInFloatBuffer(data), GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attribNo, size, GL11.GL_FLOAT, false, 0, 0);
        GL20.glEnableVertexAttribArray(attribNo);
    }

    public void dispose() {
        vaos.forEach(GL30::glDeleteVertexArrays);
        vbos.forEach(GL15::glDeleteBuffers);
        textures.forEach(GL11::glDeleteTextures);
    }

    private float[] listToFloatArray(List<Float> list) {
        float[] arr = new float[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }
}