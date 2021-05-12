package uniandes.dpoo.taller1.modelo;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Esta clase agrupa toda la información de una librería: las categorías que se
 * usan para clasificar los libros, y del catálogo de libros.
 * 
 * Adicionalmente esta clase es capaz de calcular y hacer búsquedas sobre las
 * categorías y sobre el catálogo de libros.
 */
public class Libreria { 
	// ************************************************************************
	// Atributos
	// ************************************************************************

	/**
	 * El arreglo con las categorías que hay en la librería
	 */

	private Categoria[] categorias;
	/**
	 * Una lista con los libros disponibles en la librería
	 */
	private ArrayList<Libro> catalogo;

	// ************************************************************************
	// Constructores
	// ************************************************************************

	/**
	 * Construye una nueva librería a partir de la información de los parámetros y
	 * de la información contenida en los archivos.
	 * 
	 * @param nombreArchivoCategorias El nombre del archivo CSV que tiene la
	 *                                información sobre las categorías de libros
	 * @param nombreArchivoLibros     El nombre del archivo CSV que tiene la
	 *                                información sobre los libros
	 * @throws IOException Lanza esta excepción si hay algún problema leyendo un
	 *                     archivo
	 */
	public Libreria(String nombreArchivoCategorias, String nombreArchivoLibros) throws IOException {
		this.categorias = cargarCategorias(nombreArchivoCategorias);
		this.catalogo = cargarCatalogo(nombreArchivoLibros);
	}

	// ************************************************************************
	// Métodos para consultar los atributos
	// ************************************************************************

	/**
	 * Retorna las categorías de la librería
	 * 
	 * @return categorias
	 */
	public Categoria[] darCategorias() {
		return categorias;
	}

	/**
	 * Retorna el catálogo completo de libros de la librería
	 * 
	 * @return catalogo
	 */
	public ArrayList<Libro> darLibros() {
		return catalogo;
	}

	// ************************************************************************
	// Otros métodos
	// ************************************************************************

	/**
	 * Carga la información sobre las categorías disponibles a partir de un archivo
	 * 
	 * @param nombreArchivoCategorias El nombre del archivo CSV que contiene la
	 *                                información de las categorías
	 * @return Un arreglo con las categorías que se encontraron en el archivo
	 * @throws IOException Se lanza esta excepción si hay algún problema leyendo del
	 *                     archivo
	 */
	private Categoria[] cargarCategorias(String nombreArchivoCategorias) throws IOException {
		ArrayList<Categoria> listaCategorias = new ArrayList<Categoria>();

		BufferedReader br = new BufferedReader(new FileReader(nombreArchivoCategorias));
		String linea = br.readLine(); // Ignorar la primera línea porque tiene los títulos

		linea = br.readLine();
		while (linea != null) {
			String[] partes = linea.trim().split(",");
			String nombreCat = partes[0];
			boolean esFiccion = partes[1].equals("true");

			// Crear una nueva categoría y agregarla a la lista
			listaCategorias.add(new Categoria(nombreCat, esFiccion));
			linea = br.readLine();
		}

		br.close();

		// Convertir la lista de categorías a un arreglo
		Categoria[] arregloCategorias = new Categoria[listaCategorias.size()];
		for (int i = 0; i < listaCategorias.size(); i++) {
			arregloCategorias[i] = listaCategorias.get(i);
		}

		return arregloCategorias;
	}

	/**
	 * Carga la información sobre los libros disponibles en la librería.
	 * 
	 * Se deben haber cargado antes las categorías e inicializado el atributo
	 * 'categorias'.
	 * 
	 * @param nombreArchivoLibros El nombre del archivo CSV que contiene la
	 *                            información de los libros
	 * @return Una lista con los libros que se cargaron a partir del archivo
	 * @throws IOException Se lanza esta excepción si hay algún problema leyendo del
	 *                     archivo
	 */
	private ArrayList<Libro> cargarCatalogo(String nombreArchivoLibros) throws IOException {
		ArrayList<Libro> libros = new ArrayList<Libro>();

		BufferedReader br = new BufferedReader(new FileReader(nombreArchivoLibros));
		String linea = br.readLine(); // Ignorar la primera línea porque tiene los títulos:
										// Titulo,Autor,Calificacion,Categoria,Portada,Ancho,Alto

		linea = br.readLine();
		while (linea != null) {
			String[] partes = linea.trim().split(",");
			String elTitulo = partes[0];
			String elAutor = partes[1];
			double laCalificacion = Double.parseDouble(partes[2]);
			String nombreCategoria = partes[3];
			Categoria laCategoria = buscarCategoria(nombreCategoria);
			String archivoPortada = partes[4];
			int ancho = Integer.parseInt(partes[5]);
			int alto = Integer.parseInt(partes[6]);

			// Crear un nuevo libro
			Libro nuevo = new Libro(elTitulo, elAutor, laCalificacion, laCategoria);
			libros.add(nuevo);

			// Si existe el archivo de la portada, ponérselo al libro
			if (existeArchivo(archivoPortada)) {
				Imagen portada = new Imagen(archivoPortada, ancho, alto);
				nuevo.cambiarPortada(portada);
			}

			linea = br.readLine();
		}

		br.close();

		return libros;
	}

	/**
	 * Busca una categoría a partir de su nombre
	 * 
	 * @param nombreCategoria El nombre de la categoría buscada
	 * @return La categoría que tiene el nombre dado
	 */
	private Categoria buscarCategoria(String nombreCategoria) {
		for (var c : categorias) {
			if (c.darNombre().equals(nombreCategoria)) {
				return c;
			}
		}

		return null;
	}

	/**
	 * Verifica si existe el archivo con el nombre indicado dentro de la carpeta
	 * "data".
	 * 
	 * @param nombreArchivo El nombre del archivo que se va a buscar.
	 * @return
	 */
	private boolean existeArchivo(String nombreArchivo) {
		File archivo = new File("./data/" + nombreArchivo);
		return archivo.exists();
	}

	/**
	 * Retorna una lista con los libros que pertenecen a la categoría indicada en el
	 * parámetro
	 * 
	 * @param nombreCategoria El nombre de la categoría de interés
	 * @return Una lista donde todos los libros pertenecen a la categoría indicada
	 */
	public ArrayList<Libro> darLibros(String nombreCategoria) {
		ArrayList<Libro> seleccionados = new ArrayList<Libro>();
		Categoria c = buscarCategoria(nombreCategoria);
		seleccionados.addAll(c.darLibros());
		return seleccionados;
	}

	/**
	 * Busca un libro a partir de su título
	 * 
	 * @param tituloLibro Título del libro buscado
	 * @return Retorna un libro con el título indicado o null si no se encontró un
	 *         libro con ese título
	 */
	public Libro buscarLibro(String tituloLibro) {
		for (var l : catalogo) {
			if (l.darTitulo().equals(tituloLibro)) {
				return l;
			}
		}

		return null;
	}

	/**
	 * Busca en la librería los libros escritos por el autor indicado.
	 * 
	 * El nombre del autor puede estar incompleto, y la búsqueda no debe tener en
	 * cuenta mayúsculas y minúsculas. Por ejemplo, si se buscara por "ulio v"
	 * deberían encontrarse los libros donde el autor sea "Julio Verne".
	 * 
	 * @param cadenaAutor La cadena que se usará para consultar el autor. No
	 *                    necesariamente corresponde al nombre completo de un autor.
	 * @return Una lista con todos los libros cuyo autor coincida con la cadena
	 *         indicada
	 */
	public ArrayList<Libro> buscarLibrosAutor(String cadenaAutor) {
		ArrayList<Libro> librosAutor = new ArrayList<Libro>();

		for (var c : categorias) {
			ArrayList<Libro> l = c.buscarLibrosDeAutor(cadenaAutor);
			if (l != null) {
				librosAutor.addAll(l);
			}
		}
		return librosAutor;
	}

	/**
	 * Busca en qué categorías hay libros del autor indicado.
	 * 
	 * Este método busca libros cuyo autor coincida exactamente con el valor
	 * indicado en el parámetro nombreAutor.
	 * 
	 * @param nombreAutor El nombre del autor
	 * @return Una lista con las categorías en las cuales hay al menos un libro del
	 *         autor indicado. Si no hay un libro del autor en ninguna categoría,
	 *         retorna una lista vacía.
	 */
	public ArrayList<Categoria> buscarCategoriasAutor(String nombreAutor) {
		ArrayList<Categoria> resultado = new ArrayList<Categoria>();

		for (var c : categorias) {
			if (c.hayLibroDeAutor(nombreAutor)) {
				resultado.add(c);
			}
		}

		return resultado;
	}

	/**
	 * Calcula la calificación promedio calculada entre todos los libros del
	 * catálogo
	 * 
	 * @return Calificación promedio del catálogo
	 */
	public double calificacionPromedio() {
		double count = 0;
		for (var l : catalogo) {
			count += l.darCalificacion();
		}

		return count / catalogo.size();
	}

	/**
	 * Busca cuál es la categoría que tiene más libros
	 * 
	 * @return La categoría con más libros. Si hay empate, retorna cualquiera de las
	 *         que estén empatadas en el primer lugar. Si no hay ningún libro,
	 *         retorna null.
	 */
	public Categoria categoriaConMasLibros() {
		Categoria catMax = null;
		int max = Integer.MIN_VALUE;
		for (var c : categorias) {
			int nLibros = c.contarLibrosEnCategoria();
			if (max < nLibros) {
				max = nLibros;
				catMax = c;
			}
		}
		return catMax;
	}

	/**
	 * Busca cuál es la categoría cuyos libros tienen el mayor promedio en su
	 * calificación
	 * 
	 * @return Categoría con los mejores libros
	 */
	public Categoria categoriaConMejoresLibros() {
		Categoria catMax = null;
		double max = -Double.MAX_VALUE;

		for (var c : categorias) {
			double cal = c.calificacionPromedio();
			if (max < cal) {
				max = cal;
				catMax = c;
			}
		}
		return catMax;
	}

	/**
	 * Cuenta cuántos libros del catálogo no tienen portada
	 * 
	 * @return Cantidad de libros sin portada
	 */
	public int contarLibrosSinPortada() {
		int count = 0;
		for (var l : catalogo) {
			if (!l.tienePortada()) {
				count++;
			}
		}

		return count;
	}

	/**
	 * Consulta si hay algún autor que tenga un libro en más de una categoría
	 * 
	 * @return Retorna true si hay algún autor que tenga al menos un libro en dos
	 *         categorías diferentes. Retorna false en caso contrario.
	 */
	public boolean hayAutorEnVariasCategorias() {
		HashMap<String, String> t = new HashMap<String, String>();

//		boolean cond = false;
//		for (var c : categorias) {
//			ArrayList<Libro> libros = c.darLibros();
//			for (var l : libros) {
//				String autor = l.darAutor();
//				if (t.containsKey(autor)) {
//					String res = t.get(autor);
//					if (!res.equals(c.darNombre())) {
//						cond = true;
//					}
//				} else {
//					t.put(autor, c.darNombre());
//				}
//			}
//		}
		
		boolean cond = false;
		for (var l : catalogo) {
			String autor = l.darAutor();
			if (t.containsKey(autor)) {
				String res = t.get(autor);
				if (!res.equals(l.darCategoria().darNombre())) {
					cond = true;
					break;
				}
			} else {
				t.put(autor, l.darCategoria().darNombre());
			}
		}

		return cond;
	}

}
