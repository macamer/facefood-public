import { IMG_BASE, PLACEHOLDER } from "./demoConst.js"
import { listRecipes, sample, sortByBest } from "./demoApi.js";

function buildImgCandidates(receta) {
  const main = receta.foto && receta.foto !== "main.jpg" ? receta.foto : "main.jpg";
  return [
    `${IMG_BASE}/${receta.id}/${main}`, // /<id>/main.jpg (o el nombre incluido en receta.foto)
    `${IMG_BASE}/${receta.id}.jpg`,     // /<id>.jpg
  ];
}

// Construye la etiqueta <img> con fallback a placeholder
function imgTagForRecipe(receta) {
  const candidates = buildImgCandidates(receta);
  const first = candidates[0];
  const try2  = candidates[1] || PLACEHOLDER;

  // Usamos onerror para sustituir si 404
  return `<img src="${first}"
               alt="${receta.titulo}"
               id="${receta.id}"
               class="cursor"
               onerror="this.onerror=null; this.src='${try2}'; 
              this.addEventListener('error', ()=>{ this.src='${PLACEHOLDER}'; });">`;
}

// demo.carousel.js
export async function cargarCarruselRecetasDEMO(tipo, idItem) {
  const contenedor = document.getElementById(idItem);
  if (!contenedor) return;

  const todas = await listRecipes();

  let lista = [];
  if (tipo === "findBestRecetas") {
    lista = todas.slice().sort(sortByBest).slice(0, 10);
  } else if (tipo === "findRandom") {
    lista = sample(todas, 5);
  } else {
    lista = todas.slice(0, 10);
  }

  if (!lista.length) {
    contenedor.innerHTML = `<p class="t-center">No hay recetas para mostrar.</p>`;
    return;
  }

  lista.forEach((receta, i) => {
    const card = document.createElement("div");
    card.classList.add("card-receta");
    card.style.animationDelay = `${i * 100}ms`;

    // Puedes mostrar tipo/pais/rating en la descripción (recipes.json ya trae averageRating y ratingsCount)
    const desc = `${receta.tipo || "Receta"} · ${receta.pais || ""} · ⭐ ${Number(receta.averageRating || 0).toFixed(1)}`;

    card.innerHTML = `
      ${imgTagForRecipe(receta)}
      <div class="card-body">
        <div class="card-title">${receta.titulo}</div>
        <div class="card-desc">${desc}</div>
      </div>
    `;

    card.addEventListener("click", () => {
      // Navegación estática (sin backend):
      // lleva a receta.html?id=<id>
      window.location.href = `./recipe.html?id=${receta.id}`;
    });

    contenedor.appendChild(card);
  });

  // Si ya tienes funciones de scroll/flechas, las reaprovechas:
  // setupScrollZones();
  if (typeof setupArrowButtons === "function") setupArrowButtons();
}

