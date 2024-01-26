//Seccion Menu
function showSection(sectionId) {
// Oculta todas las secciones
    var sections = document.querySelectorAll(".content-section");
    sections.forEach(function (section) {
        section.classList.remove("active-section");
    });
    // Muestra la sección específica según el enlace clicado
    var selectedSection = document.getElementById(sectionId + "Section");
    if (selectedSection) {
        selectedSection.classList.add("active-section");
    }
}


//Proyecto 
// Función para mostrar sugerencias
function mostrarSugerencias(valor) {
    console.log('Valor de búsqueda:', valor);

    // Oculta la lista de sugerencias si el valor está vacío
    if (!valor.trim()) {
        document.getElementById('sugerenciasProyectos').style.display = 'none';
        return;
    }

    // Limpia la lista de sugerencias
    var sugerenciasProyectos = document.getElementById('sugerenciasProyectos');
    sugerenciasProyectos.innerHTML = '';

    // Realiza la solicitud al controlador para obtener las sugerencias
    fetch('/admin/sugerirNombresProyectos?query=' + encodeURIComponent(valor), {
        method: 'GET', // Cambia el método a GET
    })
            .then(response => response.json())
            .then(data => {
                console.log('Datos recibidos:', data);

                // Agrega las sugerencias a la lista
                var ul = document.createElement('ul');

                // Verifica si 'data' es una cadena en lugar de una matriz
                var sugerencias = Array.isArray(data) ? data : [data];

                sugerencias.forEach(function (sugerencia) {
                    var li = document.createElement('li');
                    li.textContent = sugerencia;
                    li.onclick = function () {
                        document.getElementById('nombreProyecto').value = sugerencia;
                        sugerenciasProyectos.style.display = 'none';
                    };
                    ul.appendChild(li);
                });

                sugerenciasProyectos.appendChild(ul);

                // Muestra el div si hay sugerencias, ocúltalo de lo contrario
                sugerenciasProyectos.style.display = sugerencias.length > 0 ? 'block' : 'none';
            })
            .catch(error => console.error('Error al obtener sugerencias:', error));
}

// Función para manejar la selección de sugerencias
function seleccionarSugerencia(event) {
    var sugerenciaSeleccionada = event.target.textContent;
    document.getElementById('nombreProyecto').value = sugerenciaSeleccionada;
    document.getElementById('sugerenciasProyectos').style.display = 'none';
}

// Función para realizar la búsqueda y actualizar la vista
function buscarProyecto() {
    var valor = document.getElementById('nombreProyecto').value;
    var data = {nombreProyecto: valor};

    fetch('/admin/buscar', {
        method: 'POST', // Cambia a POST
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
            .then(response => response.json())
            .then(data => {
                // Limpiar la vista antes de actualizarla
                limpiarVista();

                // Empresas
                var empresasContainer = document.getElementById('empresasContainer');
                if (data.empresas && data.empresas.length > 0) {
                    var empresasList = document.createElement('ul');
                    data.empresas.forEach(function (empresa) {
                        var li = document.createElement('li');
                        li.textContent = empresa;
                        empresasList.appendChild(li);
                    });
                    empresasContainer.appendChild(empresasList);
                } else {
                    empresasContainer.innerHTML = '<div class="alert alert-info" style="border: 2px solid var(--fondo)">No se encontraron empresas asociadas al proyecto.</div>';
                }

                // Usuarios
                var usuariosContainer = document.getElementById('usuariosContainer');
                if (data.usuarios && data.usuarios.length > 0) {
                    var usuariosList = document.createElement('ul');
                    data.usuarios.forEach(function (usuario) {
                        var li = document.createElement('li');
                        // Lógica similar para mostrar usuarios...
                        usuariosList.appendChild(li);
                    });
                    usuariosContainer.appendChild(usuariosList);
                } else {
                    usuariosContainer.innerHTML = '<div class="alert alert-info" style="border: 2px solid var(--fondo)">No se encontraron usuarios asociados al proyecto.</div>';
                }
                // Reuniones
                var reunionesContainer = document.getElementById('reunionesContainer');
                if (data.reuniones && data.reuniones.length > 0) {
                    var reunionesList = document.createElement('ul');
                    data.reuniones.forEach(function (reunion) {
                        var li = document.createElement('li');
                        // Lógica similar para mostrar reuniones...
                        reunionesList.appendChild(li);
                    });
                    reunionesContainer.appendChild(reunionesList);
                } else {
                    reunionesContainer.innerHTML = '<div class="alert alert-info" style="border: 2px solid var(--fondo)">No se encontraron usuarios asociados al proyecto.</div>';
                }
                // Tareas
                var tareasContainer = document.getElementById('tareasContainer');
                if (data.tareas && data.tareas.length > 0) {
                    var tareasList = document.createElement('ul');
                    data.tareas.forEach(function (tarea) {
                        var li = document.createElement('li');
                        // Lógica similar para mostrar tareas...
                        tareasList.appendChild(li);
                    });
                    tareasContainer.appendChild(tareasList);
                } else {
                    tareasContainer.innerHTML = '<div class="alert alert-info" style="border: 2px solid var(--fondo)">No se encontraron usuarios asociados al proyecto.</div>';
                }

            })
            .catch(error => {
                console.error('Error en la solicitud:', error);
                // Manejar el error, mostrar un mensaje al usuario, etc.
            });
}

// Función para limpiar la vista
function limpiarVista() {
    var empresasContainer = document.getElementById('empresasContainer');
    empresasContainer.innerHTML = '';

    var usuariosContainer = document.getElementById('usuariosContainer');
    usuariosContainer.innerHTML = '';

    var reunionesContainer = document.getElementById('reunionesContainer');
    reunionesContainer.innerHTML = '';

    var tareasContainer = document.getElementById('tareasContainer');
    tareasContainer.innerHTML = '';

}

// Selecciona el formulario y asocia la función buscarProyecto al evento submit
document.getElementById('formBusquedaProyecto').addEventListener('submit', function (event) {
    // Evita que el formulario se envíe normalmente (ya que queremos manejarlo con JavaScript)
    event.preventDefault();

    // Llama a la función buscarProyecto al hacer clic en el botón
    buscarProyecto();
});

// Selecciona el formulario y asocia la función mostrarSugerencias al evento input del campo de búsqueda
document.getElementById('nombreProyecto').addEventListener('input', function () {
    mostrarSugerencias(this.value);
});


//Seccion Usuarios
function showDetails(userId) {
    console.log("Mostrando detalles para el usuario con ID:", userId);
    var postData = {
        userId: userId
    };
    fetch('/admin/dashboard', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(postData)
    })
            .then(response => {
                console.log('Respuesta del servidor:', response);
                return response.json();
            })
            .then(data => {
                console.log('Datos recibidos:', data);
                console.log('Respuesta del servidor:', data);
                var userDetailsSection = document.getElementById('detailsSection-' + userId);
                if (userDetailsSection) {
                    userDetailsSection.style.display = "block";
                    // Actualizar la vista con los datos recibidos
                    userDetailsSection.innerHTML = `
                        <h4>Proyectos</h4>
                        <p>Proyectos Totales: ${data.proyectosTotales}</p>
                        <p>Proyectos Pendientes: ${data.proyectosEnProgreso}</p>
                        <p>Proyectos Finalizados: ${data.proyectosFinalizados}</p>
                        <h4>Reuniones</h4>
                        <p>Reuniones Totales: ${data.reunionesTotales}</p>
                        <p>Reuniones Pendientes: ${data.reunionesEnProgreso}</p>
                        <p>Reuniones Finalizados: ${data.reunionesFinalizados}</p>
                        <h4>Tareas</h4>
                        <p>Tareas Totales: ${data.tareasTotales}</p>
                        <p>Tareas Pendientes: ${data.tareasEnProgreso}</p>
                        <p>Tareas Finalizados: ${data.tareasFinalizados}</p>
                        <button onclick="hideDetails()">Ver Menos</button>
                    `;
                    userDetailsSection.scrollIntoView({behavior: 'smooth'});
                } else {
                    console.error("No se encontró la sección de detalles para el usuario con ID:", userId);
                }
            })
            .catch(error => {
                console.error('Error en la solicitud POST:', error);
            });
}

function hideDetails() {
    var detailsSections = document.querySelectorAll(".detailsSection");
    detailsSections.forEach(function (section) {
        section.style.display = "none";
    });
}

//Seccion Empresas
function mostrarSugerenciasEmpresa(valor) {
// Oculta la lista de sugerencias si el valor está vacío
    if (!valor.trim()) {
        document.getElementById('sugerenciasEmpresas').style.display = 'none';

        return;
    }

// Limpia la lista de sugerencias
    var sugerenciasEmpresas = document.getElementById('sugerenciasEmpresas');
    sugerenciasEmpresas.innerHTML = '';
    // Realiza la solicitud al controlador para obtener las sugerencias de empresas a través de usuarios
    fetch('/sugerirNombresEmpresas?query=' + valor)
            .then(response => response.json())
            .then(data => {
                // Agrega las sugerencias a la lista
                var ul = document.createElement('ul');
                data.forEach(function (sugerencia) {
                    var li = document.createElement('li');
                    li.textContent = sugerencia;
                    li.onclick = function () { // Agrega un evento onclick a cada sugerencia
                        document.getElementById('nombreEmpresa').value = sugerencia;
                        sugerenciasEmpresas.style.display = 'none';
                    };
                    ul.appendChild(li);
                });
                sugerenciasEmpresas.appendChild(ul);
                // Muestra el div si hay sugerencias, ocúltalo de lo contrario
                sugerenciasEmpresas.style.display = data.length > 0 ? 'block' : 'none';
            })
            .catch(error => console.error('Error al obtener sugerencias de empresas a través de usuarios:', error));
}
////Seccion Dashboard
//


var cadenaGenerica = document.getElementById('avblAgents').textContent + " Agentes Disponibles";

console.log("Agentes count:", cadenaGenerica);


//
//
// Configuración del gráfico de carga de trabajo de los agentes
var workloadData = {
    labels: [
        "Agente 1",
        cadenaGenerica,
        "Agente 3",
        "Agente 4",
        "Agente 1",
        "Agente 2",
        "Agente 3",
        "Agente 4",
        "Agente 1",
        "Agente 2",
        "Agente 3",
        "Agente 4"
    ],
    datasets: [
        {
            label: "Carga de Trabajo",
            data: [15, 10, 8, 5, 15, 10, 8, 5, 15, 10, 8, 5],
            backgroundColor: "rgba(75, 192, 192, 0.2)",
            borderColor: "rgba(75, 192, 192, 1)",
            borderWidth: 1
        }
    ]
};

var workloadOptions = {
    scales: {
        y: {
            beginAtZero: true
        }
    }
};

var workloadChart = new Chart(document.getElementById("workloadChart"), {
    type: "bar",
    data: workloadData,
    options: workloadOptions
});

// Configuración del gráfico de estado de los proyectos
var projectStatusData = {
    labels: ["Pendiente", "En Progreso", "Completado"],
    datasets: [
        {
            data: [
                document.getElementById('pendingProjects').textContent,
                document.getElementById('inProgressProjects').textContent,
                document.getElementById('completedProjects').textContent],
            backgroundColor: [
                "rgba(255, 99, 132, 0.2)",
                "rgba(255, 205, 86, 0.2)",
                "rgba(54, 162, 235, 0.2)",
            ],
            borderColor: [
                "rgba(255, 99, 132, 1)",
                "rgba(255, 205, 86, 1)",
                "rgba(54, 162, 235, 1)",
            ],
            borderWidth: 1
        }
    ]
};

var projectStatusOptions = {
    responsive: true,
    plugins: {
        legend: {
            position: "top"
        },
        title: {
            display: true,
            text: "Estado de los Proyectos"
        }
    }
};

var projectStatusChart = new Chart(
        document.getElementById("projectStatusChart"),
        {
            type: "pie",
            data: projectStatusData,
            options: projectStatusOptions
        }
);
  