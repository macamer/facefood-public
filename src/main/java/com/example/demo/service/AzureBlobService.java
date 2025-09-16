package com.example.demo.service;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

@Service
public class AzureBlobService {
	private final BlobServiceClient blobServiceClient;
    private final String recetasContainer = "recetasimg";
    private final String avataresContainer = "avatarimg";

    public AzureBlobService(@Value("${azure.blob.connection-string}") String connectionString) {
        this.blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
    }

    public String subirImagenReceta(MultipartFile imagen, Long idReceta, String nombrePersonalizado) throws IOException {
        return subirImagen(recetasContainer, "recetas/" + idReceta + "/" + nombrePersonalizado, imagen);
    }

    public String subirAvatarUsuario(MultipartFile imagen, String nomUsuario) throws IOException {
        return subirImagen(avataresContainer, "usuarios/" + nomUsuario, imagen);
    }

    private String subirImagen(String containerName, String pathSinExtension, MultipartFile imagen) throws IOException {
        String extension = FilenameUtils.getExtension(imagen.getOriginalFilename());
        String blobName = pathSinExtension + "." + extension;

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        blobClient.upload(imagen.getInputStream(), imagen.getSize(), true);

        return blobClient.getBlobUrl(); 
    }
    
    public InputStream comprimirYConvertirImagen(MultipartFile file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(800, 800) 
                .outputFormat("jpg")
                .outputQuality(0.7) //entre 0 y 1
                .toOutputStream(baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }
    
    public String subirAvatarUsuarioJpg(MultipartFile imagen, String nomUsuario) throws IOException {
        InputStream cleanStream = comprimirYConvertirImagen(imagen);

        String extension = "jpg"; // Siempre JPG para estandarizar
        String blobName = "usuarios/" + nomUsuario + "." + extension;

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(avataresContainer);
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        // El tamaño ahora viene del ByteArrayOutputStream
        byte[] cleanBytes = cleanStream.readAllBytes();
        ByteArrayInputStream finalStream = new ByteArrayInputStream(cleanBytes);

        blobClient.upload(finalStream, cleanBytes.length, true);
        return blobClient.getBlobUrl();
    }
    
    public String subirImagenRecetaJpg(MultipartFile imagen, Long idReceta, String nombrePersonalizado) throws IOException {
        InputStream cleanStream = comprimirYConvertirImagen(imagen);

        String extension = "jpg"; 
        String blobName = "recetas/" + idReceta + "/" + nombrePersonalizado + "." + extension;

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(recetasContainer);
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        byte[] cleanBytes = cleanStream.readAllBytes();
        ByteArrayInputStream finalStream = new ByteArrayInputStream(cleanBytes);

        blobClient.upload(finalStream, cleanBytes.length, true);
        return blobClient.getBlobUrl();
    }
}
