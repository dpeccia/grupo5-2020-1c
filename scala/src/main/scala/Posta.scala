abstract class Posta (_competidores: List[Competidor],_requisito: Option[Requisito]) {
  var competidores: List[Competidor] = _competidores
  var requisito : Option[Requisito] =  _requisito

  def nivelDeHambreQueIncrementa: Int

  def ganador: Competidor = ordenarSegunCondicion.head

  def ordenarSegunCondicion: List[Competidor] = competidores.sortBy(competidor => condicionParaGanar(competidor))

  def condicionParaGanar(competidor: Competidor): Double

  //def puedeParticipar(competidor: Competidor): Boolean

  def aumentarHambre = competidores.foreach(_.incrementarNivelDeHambre(nivelDeHambreQueIncrementa))

}

case class Pesca(_competidores: List[Competidor],_requisito: Option[Requisito]) extends Posta(_competidores,_requisito) {
  override def nivelDeHambreQueIncrementa: Int = 5
  override def condicionParaGanar(competidor: Competidor): Double = competidor.cuantoPuedeCargar
  //override def puedeParticipar(competidor: Competidor): Boolean = requisito.getOrElse()
}

case class Combate(_competidores: List[Competidor],_requisito: Option[Requisito]) extends Posta(_competidores,_requisito) {
  override def nivelDeHambreQueIncrementa: Int = 10
  override def condicionParaGanar(competidor: Competidor): Double = competidor.danioQueProduce
}

case class Carrera(_competidores: List[Competidor],_requisito: Option[Requisito],km: Int) extends Posta(_competidores,_requisito) {
  override def nivelDeHambreQueIncrementa: Int = km
  override def condicionParaGanar(competidor: Competidor): Double = competidor.velocidad
}