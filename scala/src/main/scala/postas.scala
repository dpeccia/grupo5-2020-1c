abstract class Posta (_requisito: Option[RequisitoPosta]) {
  def participantes(competidores: List[Competidor]): List[Competidor] = competidores.filter(competidor => this.puedeParticipar(competidor))

  def requisito : Option[RequisitoPosta] = _requisito

  def nivelDeHambreQueIncrementa: Int

  def esMejor(competidor: Competidor, otroCompetidor: Competidor): Boolean

  def ordenarSegunQuienEsMejor(competidores: List[Competidor]): List[Competidor] = competidores.sortWith((c1,c2) => this.esMejor(c1,c2))

  def puedeParticipar(competidor: Competidor): Boolean = competidor.puedeParticipar(this) && cumpleCondicionParaParticipar(competidor)

  def cumpleCondicionParaParticipar(competidor: Competidor): Boolean = {
    if(requisito.isDefined)
      requisito.get.cumpleRequisito(competidor)
    else
      true
  }

  def aumentarHambre(competidores: List[Competidor]) = competidores.foreach(_.incrementarNivelDeHambre(nivelDeHambreQueIncrementa))

  def realizarPosta(competidores: List[Competidor]): List[Competidor] = { //quizas despues saquemos esto
    val competidoresQuePuedenRealizarLaPosta = this.participantes(competidores)
    aumentarHambre(competidoresQuePuedenRealizarLaPosta)
    ordenarSegunQuienEsMejor(competidoresQuePuedenRealizarLaPosta)
  }

}

case class Pesca(_requisito: Option[RequisitoPesoDeterminado]) extends Posta(_requisito) {
  override def nivelDeHambreQueIncrementa: Int = 5
  override def esMejor(competidor: Competidor, otroCompetidor: Competidor): Boolean =  competidor.cuantoPuedeCargar > otroCompetidor.cuantoPuedeCargar
}

// TODO: que los tipos de requisito para un combate solo puedan ser los que dice la consigna
case class Combate(_requisito: Option[RequisitoPosta]) extends Posta(_requisito) {
  override def nivelDeHambreQueIncrementa: Int = 10
  override def esMejor(competidor: Competidor, otroCompetidor: Competidor): Boolean = competidor.danioQueProduce > otroCompetidor.danioQueProduce
}

case class Carrera(_requisito: Option[RequisitoMontura],km: Int) extends Posta(_requisito) {
  override def nivelDeHambreQueIncrementa: Int = km
  override def esMejor(competidor: Competidor, otroCompetidor: Competidor): Boolean = competidor.velocidad > otroCompetidor.velocidad
}

