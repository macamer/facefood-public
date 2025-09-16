import { getPasos } from "./api.js";
import { PATH_IMG_RECETAS } from "./const.js";
import { showNameAndValiteFiles, showNameAndValiteFilesForInput } from "./photo.js";
import { crearElemento, iniciarLazyLoading } from "./ui.js";


export function addPasoContainer(addpaso, stepNumber) {
    addpaso.addEventListener("click", function (event) {
        let newPaso = document.createElement("div");
        newPaso.classList.add("pasoitem", "fx-ss-col", "mt-2", "filecontainer");

        newPaso.innerHTML = `
        <label class="smll"><strong>PASO</strong><b class="obl"> *</b></label>
        <textarea class="textarea"></textarea>
        <div class="fx-ss-col my-2">
            <label class="smll mb-1">Selecciona una imagen</label>
            <div class="file-select file-select-secondary" id="paso-file${stepNumber}">
                <input id="paso${stepNumber}" type="file" name="paso-file${stepNumber}" aria-label="Imagen">
            </div>
            <div class="fx-cs">
                <span class="file-name smll mt-1"></span>
            </div> 
        </div>
    `;

        let pasosContainer = document.getElementById("pasos-container");

        if (pasosContainer) {
            pasosContainer.appendChild(newPaso);
            stepNumber++;
            const inputFile = newPaso.querySelector('input[type="file"]');
            if (inputFile) {
                inputFile.addEventListener('change', function () {
                    showNameAndValiteFilesForInput(this);
                });
            }
        } else {
            console.error("Contenedor de pasos no encontrado");
        }
    });
}

export function removePasoContainer(removePaso) {
    removePaso.addEventListener("click", function (event) {
        let pasosContainer = document.getElementById("pasos-container");
        if (pasosContainer && pasosContainer.lastChild) {
            pasosContainer.removeChild(pasosContainer.lastChild)
        } else {
            console.error("No hay más pasos para eliminar o contenedor no encontrado")
        }
    });
}

export async function cargarPasos(idRecipe) {
    try {
        const listaPasos = await getPasos(idRecipe);
        let recipe = document.getElementById("recipe")
        // Limpiar contenido previo
        let divPaso = document.getElementById("paso")
        divPaso.innerHTML = "";

        for (let paso of listaPasos) {
            let divImg = crearElemento("div", divPaso, ["paso-img", "fade-zoom"])
            let img = crearElemento("img", divImg, ["w-150", "br-1"])
            if (!paso.foto || paso.foto == null || paso.foto == "") {
                if (paso.recetaDTO.foto == null || paso.recetaDTO.foto == "") {
                    img.src = `${PATH_IMG_RECETAS}default.png`
                }
                img.src = `${paso.recetaDTO.foto}`
            } else {
                img.src = `${paso.foto}`;
            }
            img.alt = `Paso ${paso.num}`;

            // Si la imagen no carga, usa una imagen por defecto
            img.onerror = function () {
                console.warn(`No se encontró la imagen: ${img.src}, usando imagen por defecto.`);
                img.src = `${PATH_IMG_RECETAS}default.png`
                console.warn(`No se encontró la imagen: ${img.src}, usando imagen por defecto.`);
            };
            img.setAttribute("loading", "lazy")

            let divText = crearElemento("div", divPaso, ["paso-text"])
            let title = crearElemento("h3", divText, ["paso-title"])
            let txt = crearElemento("p", divText, ["paso-p"])

            title.innerText = paso.num
            txt.innerText = paso.explicacion
            divPaso = crearElemento("div", recipe, ["paso", "sm-display-block"])
        }

        iniciarLazyLoading()

    } catch (error) {
        console.error("Error al cargar ingredientes:", error);
    }
}

export function fillPasos(listaPasos, idReceta) {
    const pasosContainer = document.getElementById('pasos-container');
    pasosContainer.innerHTML = ''; // Limpiamos por si hay pasos vacíos al inicio

    let stepNumber = 1;

    listaPasos.sort((a, b) => a.num - b.num); 

    listaPasos.forEach(paso => {
        // Crear el bloque visual de un paso
        const pasoDiv = document.createElement('div');
        pasoDiv.classList.add('pasoitem', 'fx-ss-col', 'mt-2', 'filecontainer');

        // Label del paso
        const label = document.createElement('label');
        label.classList.add('smll');
        label.innerHTML = `<strong>PASO</strong><b class="obl"> *</b>`;

        // Textarea de la explicación
        const textarea = document.createElement('textarea');
        textarea.id = `paso${stepNumber}`;
        textarea.classList.add('textarea');
        textarea.value = paso.explicacion || '';

        // Contenedor del file input
        const fileContainer = document.createElement('div');
        fileContainer.classList.add('fx-ss-col', 'my-2');

        const labelFile = document.createElement('label');
        labelFile.classList.add('smll', 'mb-1');
        labelFile.textContent = 'Selecciona una imagen';

        const fileSelectDiv = document.createElement('div');
        fileSelectDiv.classList.add('file-select', 'file-select-secondary');
        fileSelectDiv.id = `paso-file${stepNumber}`;

        const inputFile = document.createElement('input');
        inputFile.type = 'file';
        inputFile.name = `paso-file${stepNumber}`;
        inputFile.setAttribute('aria-label', 'Imagen');
        inputFile.accept = 'image/*';

        fileSelectDiv.appendChild(inputFile);
        fileContainer.appendChild(labelFile);
        fileContainer.appendChild(fileSelectDiv);

        const div = document.createElement('fx-cs');
        div.classList.add('fx-cs', "mt-2")
        fileContainer.appendChild(div)

        // Nombre del archivo subido 
        const fileName = document.createElement('span');
        fileName.classList.add('file-name', 'smll', 'mt-1');
        fileName.textContent = paso.foto ? `📷` : "";

        div.appendChild(fileName);

        // Si el paso ya tiene imagen, mostrar la preview inicial
        if (paso.foto && paso.foto.trim() !== "") {
            const imgPreview = document.createElement('img');
            imgPreview.classList.add('img-preview');
            imgPreview.style.width = '100px';
            imgPreview.style.marginTop = '8px';
            imgPreview.style.borderRadius = '8px';
            imgPreview.src = `${paso.foto}`; 
            fileContainer.appendChild(imgPreview);

            // Botón borrar
            const btnDelete = document.createElement('button');
            btnDelete.type = 'button';
            btnDelete.classList.add('delete-photo-btn');
            btnDelete.innerHTML = '<i class="bi bi-trash3"></i>';
            btnDelete.style.marginLeft = '6px';
            btnDelete.title = "Eliminar esta foto";

            btnDelete.addEventListener('click', function () {
                fileName.textContent = "";
                imgPreview?.remove();
                btnDelete.remove();
                inputFile.value = "";
                pasoDiv.setAttribute('data-foto-borrada', 'true');
            });

            div.appendChild(btnDelete);
        }

        // Agrupar todo
        pasoDiv.appendChild(label);
        pasoDiv.appendChild(textarea);
        pasoDiv.appendChild(fileContainer);

        pasosContainer.appendChild(pasoDiv);

        stepNumber++;

        inputFile.addEventListener('change', function () {
            pasoDiv.removeAttribute('data-foto-borrada');
            showNameAndValiteFilesForInput(this)
        });
    });

}

