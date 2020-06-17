abstract class Posta (_competidores: List[Competidor],_requisito: Option[RequisitoPosta]) {
  var competidores: List[Competidor] = _competidores.filter(competidor => this.puedeParticipar(competidor))
  var requisito : Option[RequisitoPosta] =  _requisito

  def nivelDeHambreQueIncrementa: Int

  def ganador: Competidor = ordenarSegunCondicion.head

  def mejorCompetidor(unosCompetidores: List[Competidor]): Competidor = unosCompetidores.maxBy(c => condicionParaGanar(c))

  def ordenarSegunCondicion: List[Competidor] = competidores.sortBy(competidor => condicionParaGanar(competidor))

  def condicionParaGanar(competidor: Competidor): Double

  def puedeParticipar(competidor: Competidor): Boolean = competidor.puedoParticipar(this) && cumpleCondicionParaParticipar(competidor)

  def cumpleCondicionParaParticipar(competidor: Competidor): Boolean = requisito.get.cumpleRequisito(competidor) //Todo ver si rompe si no tiene requisitos, Hacer pruebas

  def aumentarHambre = competidores.foreach(_.incrementarNivelDeHambre(nivelDeHambreQueIncrementa))

  def realizarPosta: List[Competidor] = { //quizas despues saquemos esto
    aumentarHambre
    ordenarSegunCondicion
  }

}

case class Pesca(_competidores: List[Competidor],_requisito: Option[RequisitoPosta]) extends Posta(_competidores,_requisito) {
  override def nivelDeHambreQueIncrementa: Int = 5
  override def condicionParaGanar(competidor: Competidor): Double = competidor.cuantoPuedeCargar
}

case class Combate(_competidores: List[Competidor],_requisito: Option[RequisitoPosta]) extends Posta(_competidores,_requisito) {
  override def nivelDeHambreQueIncrementa: Int = 10
  override def condicionParaGanar(competidor: Competidor): Double = competidor.danioQueProduce
}

case class Carrera(_competidores: List[Competidor],_requisito: Option[RequisitoPosta],km: Int) extends Posta(_competidores,_requisito) {
  override def nivelDeHambreQueIncrementa: Int = km
  override def condicionParaGanar(competidor: Competidor): Double = competidor.velocidad
}