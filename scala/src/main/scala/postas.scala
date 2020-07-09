trait Posta {

  def participantes(competidores: List[Competidor]): List[Competidor] = competidores.filter(competidor => this.puedeParticipar(competidor))

  def requisito: Option[RequisitoPosta]

  def nivelDeHambreQueIncrementa: Int

  def esMejor(competidor: Competidor, otroCompetidor: Competidor): Boolean = condicion(competidor) > condicion(otroCompetidor)

  def condicion(competidor: Competidor): Double

  def ordenarSegunQuienEsMejor(competidores: List[Competidor]): List[Competidor] = competidores.sortWith(esMejor)

  def puedeParticipar(competidor: Competidor): Boolean = competidor.puedeParticipar(this) && cumpleCondicionParaParticipar(competidor)

  def cumpleCondicionParaParticipar(competidor: Competidor): Boolean = requisito.forall(_.apply(competidor))

  def aumentarHambre(competidores: List[Competidor]): List[Competidor] = competidores.map(_.incrementarNivelDeHambre(nivelDeHambreQueIncrementa))

  def realizarPosta(competidores: List[Competidor]): List[Vikingo] = {
    val competidoresQuePuedenRealizarLaPosta = this.participantes(competidores)
    ordenarSegunQuienEsMejor(aumentarHambre(competidoresQuePuedenRealizarLaPosta)).map(_.vikingoAsociado)
    //val vikingosOrdenados = ordenarSegunQuienEsMejor(aumentarHambre(competidoresQuePuedenRealizarLaPosta)).map(_.vikingoAsociado)
    //vikingosOrdenados.filter(_.)
    //Option(vikingosOrdenados).filter(_.nonEmpty)
  }

}

case class Pesca(requisito: Option[RequisitoPesoDeterminadoPosta]) extends Posta {
  def nivelDeHambreQueIncrementa: Int = 5
  def condicion(competidor: Competidor): Double = competidor.cuantoPuedeCargar
}

case class Combate(_requisito: Option[RequisitoBarbarosidadPosta]) extends Posta {
  def requisito : Option[RequisitoPosta] = Some(_requisito.getOrElse(RequisitoArma()))
  def nivelDeHambreQueIncrementa: Int = 10
  def condicion(competidor: Competidor): Double = competidor.danioQueProduce
}

case class Carrera(requisito: Option[RequisitoMontura],km: Int) extends Posta {
  def nivelDeHambreQueIncrementa: Int = km
  def condicion(competidor: Competidor): Double = competidor.velocidad
}

