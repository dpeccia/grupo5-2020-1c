abstract class Posta (_competidores: List[Competidor]) {
  var competidores: List[Competidor] = _competidores

  def ganador: Competidor = ordenarSegunCondicion.head

  def ordenarSegunCondicion: List[Competidor] = competidores.sortBy(competidor => condicionDePosta(competidor))

  def condicionDePosta(competidor: Competidor): Double

  def aumentarHambre(nivelDeHambre: Double) = competidores.foreach(_.incrementarNivelDeHambre(nivelDeHambre))

}

class Pesca(_competidores: List[Competidor]) extends Posta(_competidores) {
  override def condicionDePosta(competidor: Competidor): Double = competidor.cuantoPuedeCargar
}

class Combate(_competidores: List[Competidor]) extends Posta(_competidores) {
  override def condicionDePosta(competidor: Competidor): Double = competidor.danioQueProduce
}

class Carrera(_competidores: List[Competidor]) extends Posta(_competidores) {
  override def condicionDePosta(competidor: Competidor): Double = competidor.velocidad
}