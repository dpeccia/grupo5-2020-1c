trait Regla {
  def prepararseParaPosta(posta: Posta, participantes: List[Vikingo], dragonesDisponiblesPosta: List[Dragon]): List[Competidor]
  def pasanALaSiguientePosta(posta: Posta, participantes: List[Competidor]): Option[List[Vikingo]]
  def desempatar(ganadores: List[Vikingo]): Option[Vikingo]
}

class Estandar() extends Regla {

  def prepararseParaPosta(posta: Posta, participantes: List[Vikingo], dragonesDisponiblesPosta: List[Dragon]): List[Competidor] = {
    var competidores: List[Competidor] = Nil
    participantes.foldLeft(dragonesDisponiblesPosta) ((dragonesNoTomados: List[Dragon], vikingo: Vikingo) => {
      val competidor = vikingo.mejorCompetidor(dragonesNoTomados, posta)
      competidores = competidores.++(List(competidor))
      if(competidor.tenesDragon) dragonesNoTomados.filterNot(competidor.esTuDragon) else dragonesNoTomados
    })
    competidores
  }

  def pasanALaSiguientePosta(posta: Posta, participantes: List[Competidor]): Option[List[Vikingo]] = {
    val ganadoresPosta = posta.realizarPosta(participantes)
    if (ganadoresPosta.getOrElse(return None).length.equals(1)) return ganadoresPosta
    Some(filtrarCuantosPasan(ganadoresPosta.get))
  }

  def filtrarCuantosPasan(ganadoresPosta: List[Vikingo]): List[Vikingo] = ganadoresPosta.take(ganadoresPosta.length / 2)

  def desempatar(ganadores: List[Vikingo]): Option[Vikingo] = Some(ganadores.head)
}

case class Eliminacion(cantidadAEliminar: Int) extends Estandar {
  override def filtrarCuantosPasan(ganadoresPosta: List[Vikingo]): List[Vikingo] = ganadoresPosta.take(ganadoresPosta.length - cantidadAEliminar)
}

case object TorneoInverso extends Estandar {
  override def desempatar(ganadores: List[Vikingo]): Option[Vikingo] = Some(ganadores.last)
  override def filtrarCuantosPasan(ganadoresPosta: List[Vikingo]): List[Vikingo] = ganadoresPosta.takeRight(ganadoresPosta.length / 2)
}

case class BanDeDragones(condicion: Dragon => Boolean) extends Estandar {
  override def prepararseParaPosta(posta: Posta, participantes: List[Vikingo], dragonesDisponiblesPosta: List[Dragon]): List[Competidor] = {
    super.prepararseParaPosta(posta, participantes, dragonesDisponiblesPosta.filter(condicion))
  }
}

case object Handicap extends Estandar {
  override def prepararseParaPosta(posta: Posta, participantes: List[Vikingo], dragonesDisponiblesPosta: List[Dragon]): List[Competidor] = {
    super.prepararseParaPosta(posta, participantes.reverse, dragonesDisponiblesPosta)
  }
}
