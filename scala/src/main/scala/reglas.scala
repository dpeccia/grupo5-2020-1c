sealed trait Regla{
  def prepararseParaPosta(posta: Posta, participantes: List[Vikingo], dragonesDisponiblesPosta: List[Dragon]): List[Competidor]
  def pasanALaSiguientePosta(posta: Posta, participantes: List[Competidor]): Option[List[Vikingo]]
  def desempatar(ganadores: List[Vikingo]): Option[Vikingo]
}

class Estandar() extends Regla{
  def prepararseParaPosta(posta: Posta, participantes: List[Vikingo], dragonesDisponiblesPosta: List[Dragon]): List[Competidor] = {
    var competidores: List[Competidor] = Nil
    participantes.foldRight(dragonesDisponiblesPosta) ((vikingo: Vikingo, dragonesNoTomados: List[Dragon]) => {
      val competidor = vikingo.mejorCompetidor(dragonesNoTomados, posta)
      competidores = competidores.++(List(competidor))
      if(competidor.tenesDragon) dragonesNoTomados.filterNot(_.equals(competidor.esTuDragon(_))) else dragonesNoTomados
    })
    competidores
  }
  def pasanALaSiguientePosta(posta: Posta, participantes: List[Competidor]): Option[List[Vikingo]] = {
    val ganadoresPosta = posta.realizarPosta(participantes)
    if (ganadoresPosta.isEmpty) return None
    else if (ganadoresPosta.length.equals(1)) return Some(ganadoresPosta)
    Some(ganadoresPosta.take(ganadoresPosta.length / 2))
  }
  def desempatar(ganadores: List[Vikingo]): Option[Vikingo] = {
    Some(ganadores.head)
  }
}
case class Eliminacion(cantidadAEliminar: Int) extends Estandar{
  override def pasanALaSiguientePosta(posta: Posta, participantes: List[Competidor]): Option[List[Vikingo]] = {
    val ganadoresPosta = posta.realizarPosta(participantes)
    Some(ganadoresPosta.take(ganadoresPosta.length - cantidadAEliminar))
  }
}
case object TorneoInverso extends Estandar {
  override def desempatar(ganadores: List[Vikingo]): Option[Vikingo] = {
    Some(ganadores.last)
  }

  override def pasanALaSiguientePosta(posta: Posta, participantes: List[Competidor]): Option[List[Vikingo]] = {
    val ganadoresPosta = posta.realizarPosta(participantes)
    Some(ganadoresPosta.takeRight(ganadoresPosta.length / 2))
  }
}
case class BanDeDragones(condicion: Dragon => Boolean) extends Estandar {
  override def prepararseParaPosta(posta: Posta, participantes: List[Vikingo], dragonesDisponiblesPosta: List[Dragon]): List[Competidor] = {
    super.prepararseParaPosta (posta, participantes, dragonesDisponiblesPosta.filter(condicion))
  }
}
case object Handicap extends Estandar{
  override def prepararseParaPosta(posta: Posta, participantes: List[Vikingo], dragonesDisponiblesPosta: List[Dragon]): List[Competidor] = {
    super.prepararseParaPosta(posta, participantes.reverse, dragonesDisponiblesPosta)
  }
}
