abstract class Posta (_competidores: List[Competidor], _requisito: Option[RequisitoPosta]) {
  var competidores: List[Competidor] = _competidores.filter(competidor => this.puedeParticipar(competidor))

  def requisito : Option[RequisitoPosta] = _requisito

  def nivelDeHambreQueIncrementa: Int

  def esMejor(competidor: Competidor, otroCompetidor: Competidor): Boolean

  def ordenarSegunQuienEsMejor: List[Competidor] = competidores.sortWith((c1,c2) => this.esMejor(c1,c2))

  def puedeParticipar(competidor: Competidor): Boolean = competidor.puedeParticipar(this) && cumpleCondicionParaParticipar(competidor)

  def cumpleCondicionParaParticipar(competidor: Competidor): Boolean = {
    if(requisito.isDefined)
      requisito.get.cumpleRequisito(competidor)
    else
      true
  }

  def aumentarHambre = competidores.foreach(_.incrementarNivelDeHambre(nivelDeHambreQueIncrementa))

  def realizarPosta: List[Competidor] = { //quizas despues saquemos esto
    aumentarHambre
    ordenarSegunQuienEsMejor
  }

}

case class Pesca(_competidores: List[Competidor],_requisito: Option[RequisitoPesoDeterminado]) extends Posta(_competidores, _requisito) {
  override def nivelDeHambreQueIncrementa: Int = 5
  override def esMejor(competidor: Competidor, otroCompetidor: Competidor): Boolean =  competidor.cuantoPuedeCargar > otroCompetidor.cuantoPuedeCargar
}

// TODO: que los tipos de requisito para un combate solo puedan ser los que dice la consigna
case class Combate(_competidores: List[Competidor],_requisito: Option[RequisitoPosta]) extends Posta(_competidores, _requisito) {
  override def nivelDeHambreQueIncrementa: Int = 10
  override def esMejor(competidor: Competidor, otroCompetidor: Competidor): Boolean = competidor.danioQueProduce > otroCompetidor.danioQueProduce
}

case class Carrera(_competidores: List[Competidor],_requisito: Option[RequisitoMontura],km: Int) extends Posta(_competidores, _requisito) {
  override def nivelDeHambreQueIncrementa: Int = km
  override def esMejor(competidor: Competidor, otroCompetidor: Competidor): Boolean = competidor.velocidad > otroCompetidor.velocidad
}

