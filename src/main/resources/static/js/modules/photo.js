import { PATH_IMG_AVATAR } from "./const.js";
import { errorSwal } from "./ui.js";

//CAMBIAR IMAGEN INPUT
export function cambiarImagenInput(inputArchivo, imageArchivo) {
    inputArchivo.addEventListener("change", function () {
        let archivo = this.files[0];

        if (!archivo.type.match("image.*")) {
            errorSwal("No es un archivo de imagen", "Elige otro formato");
            this.value = null;
            return;
        }

        let img = new Image();
        img.src = URL.createObjectURL(archivo);

        img.onload = function () {
            let canvas = document.createElement("canvas");
            let ctx = canvas.getContext("2d");

            // Definir el tamaño cuadrado basado en el lado menor
            let size = Math.min(img.width, img.height);
            canvas.width = size;
            canvas.height = size;

            // Recortar y centrar la imagen
            let x = (img.width - size) / 2;
            let y = (img.height - size) / 2;
            ctx.drawImage(img, x, y, size, size, 0, 0, size, size);

            // Convertir el canvas a URL y mostrarla
            imageArchivo.src = canvas.toDataURL("image/png");
        };
    });
}

export function mostrarAvatar(avatarImg, usuarioSession) {
    if (!usuarioSession || !usuarioSession.avatar) {
        avatarImg.src = "/facefood/img/avatar/default.png";
        avatarImg.alt = "default.jpg";
        avatarImg.classList.add('fade-zoom')
        //avatarImg.style.opacity = 0;
        return;
    }
    avatarImg.src = `${usuarioSession.avatar}`;
    avatarImg.alt = usuarioSession.avatar;
    avatarImg.classList.add('fade-zoom')
    avatarImg.onerror = function () {
        this.onerror = null; // Evita llamadas infinitas en caso de error
        this.src = "/facefood/img/avatar/default.jpg";
    }
}

export async function revisarYCortarImagenCuadrada(mainImg, previewElement, errors, mensaje) {
    if (mainImg.files.length > 0) {
        try {
            return await recortarImagenCuadrada(mainImg.files[0]);
        } catch (err) {
            console.error("Error al recortar imagen principal:", err);
            errors.push({ field: mainImg, message: mensaje });
            return mainImg.files[0];
        }
    } else {
        errors.push({ field: mainImg, message: mensaje });
        return null;
    }
}

export function recortarImagenCuadrada(fileOriginal) {
    return new Promise((resolve, reject) => {
        if (!fileOriginal.type.match("image.*")) {
            errorSwal("No es un archivo de imagen", "Elige otro formato");
            this.value = null;
            return;
        }

        const img = new Image();
        img.src = URL.createObjectURL(fileOriginal);

        img.onload = function () {
            const size = Math.min(img.width, img.height);
            const canvas = document.createElement("canvas");
            canvas.width = size;
            canvas.height = size;
            const ctx = canvas.getContext("2d");

            const x = (img.width - size) / 2;
            const y = (img.height - size) / 2;
            ctx.drawImage(img, x, y, size, size, 0, 0, size, size);

            canvas.toBlob(function (blob) {
                if (!blob) {
                    reject("No se pudo convertir el canvas a blob");
                    return;
                }

                const archivoRecortado = new File([blob], fileOriginal.name, {
                    type: "image/png",
                    lastModified: Date.now(),
                });

                resolve(archivoRecortado);
            }, "image/png");
        };

        img.onerror = () => reject("No se pudo cargar la imagen");
    });
}

export function showNameAndValiteFiles() {
    document.querySelectorAll('input[type="file"]').forEach(input => {
        input.addEventListener('change', function () {
            const file = this.files[0];
            const filecontainer = this.closest('.filecontainer');
            const fileNameSpan = filecontainer.querySelector('.file-name');

            // Elimina previews previos
            filecontainer.querySelectorAll('.img-preview, .delete-photo-btn').forEach(el => el.remove());

            if (file) {
                const allowedTypes = ['image/jpeg', 'image/png', 'image/jpg', 'image/webp'];
                const MAX_SIZE = 5 * 1024 * 1024; // 5 MB
                if (!allowedTypes.includes(file.type)) {
                    fileNameSpan.textContent = "❌ Formato no permitido. Usa JPG, PNG o WEBP.";
                    fileNameSpan.style.color = "red";
                    this.value = ""; // limpia el input
                    return;
                }

                if (file.size > MAX_SIZE) {
                    fileNameSpan.textContent = "❌ Imagen demasiado grande (máx 5MB)";
                    fileNameSpan.style.color = "red";
                    this.value = "";
                    return;
                }

                const img = new Image();
                img.src = URL.createObjectURL(file);
                img.onload = function () {
                    if (img.width < 300 || img.height < 300) {
                        fileNameSpan.textContent = "❌ Imagen demasiado pequeña (mín 300x300 px)";
                        fileNameSpan.style.color = "red";
                        input.value = "";
                        return;
                    }
                    if (img.width > 5000 || img.height > 5000) {
                        fileNameSpan.textContent = "❌ Imagen demasiado grande (máx 5000x5000 px)";
                        fileNameSpan.style.color = "red";
                        input.value = "";
                        return;
                    }
                    fileNameSpan.textContent = `✅ ${file.name}`;
                    fileNameSpan.style.color = "green";

                    // Botón borrar
                    const btnDelete = document.createElement('button');
                    btnDelete.type = 'button';
                    btnDelete.classList.add('delete-photo-btn');
                    btnDelete.innerHTML = '<i class="bi bi-trash3"></i>';
                    btnDelete.style.marginLeft = '6px';
                    btnDelete.title = "Eliminar esta foto";

                    btnDelete.addEventListener('click', function () {
                        fileNameSpan.textContent = "";
                        imgPreview?.remove();
                        btnDelete.remove();
                        input.value = "";
                    });

                    // Mete el botón justo después del span
                    fileNameSpan.parentNode.appendChild(btnDelete);

                    // Preview miniatura (debajo)
                    const imgPreview = document.createElement('img');
                    imgPreview.classList.add('img-preview');
                    imgPreview.style.width = '100px';
                    imgPreview.style.marginTop = '8px';
                    imgPreview.style.borderRadius = '8px';
                    imgPreview.src = img.src;
                    filecontainer.appendChild(imgPreview);

                };
                img.onerror = function () {
                    fileNameSpan.textContent = "❌ Archivo corrupto o no es una imagen válida.";
                    fileNameSpan.style.color = "red";
                    input.value = "";
                };

            } else {
                fileNameSpan.textContent = "";
            }
        });
    });
}

export function showNameAndValiteFilesForInput(input) {
    const file = input.files[0];
    const filecontainer = input.closest('.filecontainer');
    const fileNameSpan = filecontainer.querySelector('.file-name');

    // Elimina previews previos
    filecontainer.querySelectorAll('.img-preview').forEach(img => img.remove());

    if (file) {
        const allowedTypes = ['image/jpeg', 'image/png', 'image/jpg', 'image/webp'];
        const MAX_SIZE = 5 * 1024 * 1024; // 5 MB

        if (!allowedTypes.includes(file.type)) {
            fileNameSpan.textContent = "❌ Formato no permitido. Usa JPG, PNG o WEBP.";
            fileNameSpan.style.color = "red";
            input.value = ""; // limpia el input
            return;
        }

        if (file.size > MAX_SIZE) {
            fileNameSpan.textContent = "❌ Imagen demasiado grande (máx 5MB)";
            fileNameSpan.style.color = "red";
            input.value = "";
            return;
        }

        const img = new Image();
        img.src = URL.createObjectURL(file);
        img.onload = function () {
            if (img.width < 300 || img.height < 300) {
                fileNameSpan.textContent = "❌ Imagen demasiado pequeña (mín 300x300 px)";
                fileNameSpan.style.color = "red";
                input.value = "";
                return;
            }
            if (img.width > 5000 || img.height > 5000) {
                fileNameSpan.textContent = "❌ Imagen demasiado grande (máx 5000x5000 px)";
                fileNameSpan.style.color = "red";
                input.value = "";
                return;
            }

            fileNameSpan.textContent = `✅ ${file.name}`;
            fileNameSpan.style.color = "green";

            // Botón borrar
            const btnDelete = document.createElement('button');
            btnDelete.type = 'button';
            btnDelete.classList.add('delete-photo-btn');
            btnDelete.innerHTML = '<i class="bi bi-trash3"></i>';
            btnDelete.style.marginLeft = '6px';
            btnDelete.title = "Eliminar esta foto";

            btnDelete.addEventListener('click', function () {
                fileNameSpan.textContent = "";
                imgPreview?.remove();
                btnDelete.remove();
                input.value = "";
            });

            // Mete el botón justo después del span
            fileNameSpan.parentNode.appendChild(btnDelete);

            // Preview miniatura 
            const imgPreview = document.createElement('img');
            imgPreview.classList.add('img-preview');
            imgPreview.style.width = '100px';
            imgPreview.style.marginTop = '8px';
            imgPreview.style.borderRadius = '8px';
            imgPreview.src = img.src;
            filecontainer.appendChild(imgPreview);
        };
        img.onerror = function () {
            fileNameSpan.textContent = "❌ Archivo corrupto o no es una imagen válida.";
            fileNameSpan.style.color = "red";
            input.value = "";
        };

    } else {
        fileNameSpan.textContent = "";
    }
}
