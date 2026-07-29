<p align="center">
  <img src="https://pablot18.github.io/icc-est-practicas/assets/ups-svg-plain.svg" alt="Logo UPS" width="180"/>
</p>

<h1 align="center">Universidad Politécnica Salesiana</h1>
<h3 align="center">Carrera de Computación</h3>
<h3 align="center">Asignatura: Estructura de Datos</h3>

<h2 align="center">Proyecto Final</h2>
<h3 align="center">Implementación y visualización de rutas en un mapa de calles mediante BFS y DFS</h3>

**Integrantes:**
Erick Chang
| Elvis tipanta 

**Docente:** Ing. Pablo Torres

---

## Índice

1. [Objetivo](#objetivo)
2. [Descripción del problema](#descripción-del-problema)
3. [Marco teórico](#marco-teórico)
4. [Tecnologías utilizadas](#tecnologías-utilizadas)
5. [Diagrama UML](#diagrama-uml)
6. [Arquitectura y estructura de carpetas](#arquitectura-y-estructura-de-carpetas)
7. [Explicación general del funcionamiento](#explicación-general-del-funcionamiento)
8. [Capturas de configuraciones de mapa](#capturas-de-configuraciones-de-mapa)
9. [Ejemplo comentado de un algoritmo](#ejemplo-comentado-de-un-algoritmo)
10. [Tabla comparativa de resultados](#tabla-comparativa-de-resultados)
11. [Análisis de resultados](#análisis-de-resultados)
12. [Conclusiones individuales](#conclusiones-individuales)
13. [Recomendaciones y aplicaciones futuras](#recomendaciones-y-aplicaciones-futuras)
14. [Cómo ejecutar el proyecto](#cómo-ejecutar-el-proyecto)

---

## Objetivo

- Desarrollar una aplicación en Java que modele un mapa de calles como un grafo.
- Representar intersecciones mediante nodos posicionados manualmente sobre una imagen de fondo, y las calles mediante aristas.
- Implementar los algoritmos de búsqueda **BFS** y **DFS** para encontrar una ruta entre un nodo de inicio y un nodo de destino.
- Visualizar el comportamiento de ambos algoritmos en modo exploración y en modo ruta final.
- Aplicar estructuras de datos, persistencia de información, patrón MVC, control de versiones y documentación técnica.
- Comparar el comportamiento y los tiempos de ejecución de BFS y DFS sobre diferentes configuraciones del grafo.

## Descripción del problema

Encontrar una ruta entre dos puntos de una ciudad es un problema clásico que puede modelarse mediante teoría de grafos: cada intersección es un nodo, y cada calle que conecta dos intersecciones es una arista. Dependiendo del algoritmo de búsqueda utilizado, el orden de exploración y las rutas encontradas pueden variar, incluso partiendo del mismo punto de origen y destino.

Este proyecto busca resolver ese problema sobre un mapa real (Cuenca, Ecuador), permitiendo al usuario definir manualmente los nodos y calles sobre una imagen, y comparar visualmente cómo **BFS** (que explora por niveles) y **DFS** (que explora en profundidad con retroceso) recorren el mismo grafo para llegar del punto A al punto B.

## Marco teórico

**Grafo:** estructura de datos compuesta por un conjunto de nodos (vértices) y un conjunto de conexiones (aristas) entre ellos. Puede ser dirigido o no dirigido, y las aristas pueden o no tener peso. En este proyecto se usa un grafo no ponderado, con aristas bidireccionales (`addEdge`) o de un solo sentido (`addEdgeUni`).

**BFS (Breadth-First Search / Búsqueda en Anchura):** algoritmo de recorrido que explora el grafo nivel por nivel, utilizando una estructura de tipo **cola (FIFO)**. Garantiza encontrar la ruta con menor número de aristas (saltos) entre el nodo de inicio y el nodo de destino, cuando el grafo no está ponderado.

**DFS (Depth-First Search / Búsqueda en Profundidad):** algoritmo de recorrido que explora una rama del grafo hasta el final antes de retroceder (backtracking) y probar otra rama. Se implementa habitualmente con **recursividad** o con una **pila explícita**. No garantiza la ruta más corta, pero sí encuentra una ruta válida si existe.

**Complejidad:** ambos algoritmos tienen complejidad **O(V + E)**, donde V es el número de nodos (vértices) y E el número de aristas, ya que cada nodo y cada arista se visita como máximo una vez.

## Tecnologías utilizadas

- **Java** (JDK 26)
- **Swing** — interfaz gráfica de escritorio
- **Git / GitHub** — control de versiones
- Persistencia en archivo de texto plano (**CSV**)

## Diagrama UML

> _[Pendiente: insertar imagen del diagrama de clases, por ejemplo generado con draw.io o PlantUML, mostrando `Node<T>`, `Graph<T>`, `PathFinder<T>`, `PathResult<T>`, `BFSPathFinder`, `DFSPathFinder`, `MapPoint`, `GraphRepository`, `FileGraphRepository`, `MapController`, `MainFrame`, `MapPanel` y sus relaciones]_

**Relaciones principales:**
- `BFSPathFinder<T>` y `DFSPathFinder<T>` implementan `PathFinder<T>`.
- `Graph<T>` contiene una colección de `Node<T>`, cada uno asociado a un `Set<Node<T>>` (sus vecinos).
- `Node<T>` envuelve un valor genérico `T` (en este proyecto, `MapPoint`).
- `FileGraphRepository` implementa `GraphRepository<MapPoint>`.
- `MapController` coordina `Graph<MapPoint>`, `PathFinder<MapPoint>` y `GraphRepository<MapPoint>` con la vista (`MainFrame`, `MapPanel`).

## Arquitectura y estructura de carpetas

El proyecto sigue el patrón **Modelo-Vista-Controlador (MVC)**:

- **Modelo:** `MapPoint`, `VisualizationMode`, `Node<T>`, `Graph<T>`, `PathResult<T>`.
- **Vista:** `MainFrame`, `MapPanel` — no contienen lógica de BFS/DFS.
- **Controlador:** `MapController` — coordina interacción del usuario, ejecución de algoritmos, persistencia y actualización de la vista.

```
src/
├── app/
│   └── App.java
├── controllers/
│   └── MapController.java
├── models/
│   ├── MapPoint.java
│   └── VisualizationMode.java
├── persistence/
│   ├── GraphRepository.java
│   └── FileGraphRepository.java
├── structures/
│   ├── node/
│   │   └── Node.java
│   └── graphs/
│       ├── Graph.java
│       ├── PathFinder.java
│       ├── PathResult.java
│       └── implementations/
│           ├── BFSPathFinder.java
│           └── DFSPathFinder.java
├── views/
│   ├── MainFrame.java
│   └── MapPanel.java
resources/
└── maps/
    └── map.png
```

## Explicación general del funcionamiento

1. Al iniciar, `App.java` construye la ventana principal (`MainFrame`) y `MapController` carga la configuración guardada mediante `FileGraphRepository.load()`, reconstruyendo el `Graph<MapPoint>`.
2. El usuario puede definir nuevos nodos sobre la imagen del mapa (`MapPanel`), asignarles un identificador y coordenadas, y conectar nodos mediante aristas bidireccionales o unidireccionales.
3. El usuario selecciona un nodo de inicio (A) y un nodo de destino (B), elige el algoritmo (BFS o DFS) y el modo de visualización (exploración o ruta final).
4. `MapController` invoca `PathFinder<MapPoint>.find(graph, start, end)`, que devuelve un `PathResult<MapPoint>` con los nodos visitados, la ruta encontrada y el tiempo de ejecución.
5. Según el modo elegido, la vista anima progresivamente los nodos visitados (`EXPLORATION`) o solo la ruta final reconstruida (`FINAL_PATH`).
6. Cada vez que se agrega, edita o elimina un nodo/conexión, `MapController` llama a `FileGraphRepository.save()` para actualizar el archivo de configuración.

## Capturas de configuraciones de mapa

> _[Pendiente: insertar al menos 2 capturas de pantalla mostrando configuraciones distintas del grafo sobre el mapa — diferente cantidad de nodos y conexiones en cada una]_

**Configuración 1:**

_[captura aquí]_

**Configuración 2:**

_[captura aquí]_

## Ejemplo comentado de un algoritmo

Se explica el funcionamiento interno de `BFSPathFinder<T>`:

```java
@Override
public PathResult<T> find(Graph<T> graph, T start, T end) {
    Node<T> startNode = new Node<>(start);
    Node<T> endNode = new Node<>(end);

    Queue<Node<T>> queue = new LinkedList<>();       // (1) cola FIFO de pendientes
    Set<Node<T>> visitados = new LinkedHashSet<>();  // (2) visitados, en orden de descubrimiento
    Map<Node<T>, Node<T>> predecesores = new HashMap<>(); // (3) para reconstruir la ruta

    queue.add(startNode);
    visitados.add(startNode);

    while (!queue.isEmpty()) {
        Node<T> current = queue.poll();       // (4) siempre se saca el más antiguo -> nivel por nivel

        if (current.equals(endNode)) break;   // (5) destino encontrado, se corta la búsqueda

        for (Node<T> vecino : graph.getGraph().get(current)) {
            if (!visitados.contains(vecino)) {
                visitados.add(vecino);
                predecesores.put(vecino, current); // (6) se recuerda quién lo descubrió
                queue.add(vecino);
            }
        }
    }
    // (7) reconstrucción: desde "end" se retrocede por predecesores hasta "start", y se invierte
}
```

**Explicación paso a paso:**
1. La `Queue` asegura que los nodos se procesen en el orden en que fueron descubiertos (FIFO), lo que produce la exploración por niveles característica de BFS.
2. `LinkedHashSet` conserva el orden real de descubrimiento, necesario para animar el modo `EXPLORATION`.
3. El mapa de predecesores es la clave para reconstruir la ruta sin tener que guardar el camino completo en cada nodo de la cola.
4-5. Se detiene apenas se extrae el nodo destino de la cola (no apenas se descubre), lo que garantiza que su predecesor ya quedó registrado correctamente.
6. Cada vecino nuevo guarda quién lo descubrió — este es el dato que permite "caminar hacia atrás" al reconstruir la ruta.
7. La ruta se arma recorriendo `predecesores` desde `end` hasta `start`, y luego se invierte para que quede en el orden correcto A → B.

## Tabla comparativa de resultados

> **Importante:** estos datos deben completarse con ejecuciones reales del programa, no deben inventarse.

| Caso | Algoritmo | Inicio | Destino | Nodos visitados | Cantidad de aristas (ruta) | Tiempo |
|---|---|---|---|---|---|---|
| 1 | BFS | | | | | |
| 1 | DFS | | | | | |
| 2 | BFS | | | | | |
| 2 | DFS | | | | | |
| 3 | BFS | | | | | |
| 3 | DFS | | | | | |

## Análisis de resultados

> _[Completar después de correr las pruebas reales]_

- ¿Qué diferencias se observaron en el orden de exploración de BFS y DFS?
- ¿BFS encontró una ruta con menor cantidad de aristas en todos los casos evaluados?
- ¿DFS encontró rutas diferentes a las obtenidas con BFS?
- ¿Qué algoritmo visitó más nodos en cada caso?
- ¿Los tiempos de ejecución fueron suficientes para determinar cuál algoritmo es mejor?
- ¿Cómo influyó la estructura del grafo en el comportamiento de cada algoritmo?
- ¿Qué ventajas aporta separar la lógica del algoritmo de la visualización?
- ¿Qué mejoras podrían implementarse para trabajar con calles ponderadas?

## Conclusiones individuales

**Conclusión de Erick Chang:**
_[pendiente]_

**Conclusión de Elvis Tipanta :**
_[pendiente]_

## Recomendaciones y aplicaciones futuras

- Validar la configuración del grafo antes de construirlo, para evitar estados inconsistentes.
- Evitar que las clases de algoritmos (`BFSPathFinder`, `DFSPathFinder`) dibujen directamente componentes gráficos.
- Utilizar identificadores únicos y consistentes para todos los nodos del mapa.
- Medir el tiempo de ejecución de los algoritmos sin incluir animaciones ni operaciones de dibujo, para no distorsionar la comparación.
- Probar el sistema con grafos conectados, desconectados y con ciclos.
- Como aplicación futura, se podría extender el proyecto para trabajar con **calles ponderadas** (distancia, tráfico, tiempo estimado), lo que permitiría implementar algoritmos como Dijkstra o A* sobre la misma base de `Graph<T>`.

## Cómo ejecutar el proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Kochito-07/proyecto-estructura-mapaCuenca.git
   ```
2. Abrir la carpeta en VS Code (con el Extension Pack for Java) o en NetBeans.
3. Ejecutar la clase `src/app/App.java`.

**Requisitos:** JDK 26 o superior.