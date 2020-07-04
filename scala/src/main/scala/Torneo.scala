case class Equipo(vikingos: List[Vikingo]) {
  def cantidadDeGanadores(ganadoresDelTorneo: List[Vikingo]): Int = ganadoresDelTorneo.count(_.esDeEquipo(this))
}

case class Torneo(postas: List[Posta], dragonesDisponibles: List[Dragon]) {

  def realizarTorneoIndividualmente(participantes: List[Vikingo], regla: Regla): Option[Vikingo] = {
    obtenerGanadores(participantes, regla) match {
      case Some(ganadores) if ganadores.length.equals(1) => Some(ganadores.head)
      case Some(ganadores) => desempatar(ganadores, regla)
      case _ => None
    }
  }

  def realizarTorneoPorEquipos(participantes: List[Equipo]): Option[Equipo] = {
    obtenerGanadores(participantes.flatMap(_.vikingos), Estandar) match {
      case Some(ganadores) if ganadores.length.equals(1) => Some(Equipo(ganadores))
      case Some(ganadores) => Some(participantes.maxBy(_.cantidadDeGanadores(ganadores)))
      case _ => None
    }
  }

  // TODO: ver si hay un solo ganador en una posta, y no seguir las demas
  def obtenerGanadores(participantes: List[Vikingo], regla: Regla): Option[List[Vikingo]] = {
    postas.foldRight(Option(participantes))((posta, resultadoAnterior) => {
      val ganadoresPostaAnterior = prepararseParaPostaSegunRegla(posta, resultadoAnterior.getOrElse(return None), dragonesDisponibles, regla)
      pasanALaSiguientePosta(posta, ganadoresPostaAnterior, regla)
    })
  }

  // Etapas del torneo

  def prepararseParaPostaSegunRegla(posta: Posta, participantes: List[Vikingo], dragonesDisponiblesPosta: List[Dragon], regla: Regla): List[Competidor] = regla match {
    case BanDeDragones(condicion) => prepararseParaPosta(posta, participantes, dragonesDisponiblesPosta.filter(condicion))
    case Handicap => prepararseParaPosta(posta, participantes.reverse, dragonesDisponiblesPosta)
    case _ => prepararseParaPosta(posta, participantes, dragonesDisponiblesPosta)
  }

  def prepararseParaPosta(posta: Posta, participantes: List[Vikingo], dragonesDisponiblesPosta: List[Dragon]): List[Competidor] = {
    var competidores: List[Competidor] = Nil
    participantes.foldRight(dragonesDisponiblesPosta) ((vikingo: Vikingo, dragonesNoTomados: List[Dragon]) => {
      val competidor = vikingo.mejorCompetidor(dragonesNoTomados, posta)
      competidores = competidores.++(List(competidor))
      dragonesNoTomados.filterNot(_.equals(competidor.dragonAsociado.get))
    })
    competidores
  }

  def pasanALaSiguientePosta(posta: Posta, participantes: List[Competidor], regla: Regla): Option[List[Vikingo]] = regla match {
    case Eliminacion(cantidadAEliminar) => Some(posta.realizarPosta(participantes).take(cantidadAEliminar))
    case TorneoInverso =>
      val ganadoresPosta = posta.realizarPosta(participantes)
      Some(ganadoresPosta.takeRight(ganadoresPosta.length / 2))
    case _ =>
      val ganadoresPosta = posta.realizarPosta(participantes)
      Some(ganadoresPosta.take(ganadoresPosta.length / 2))
  }

  def desempatar(ganadores: List[Vikingo], regla: Regla): Option[Vikingo] = regla match {
    case TorneoInverso => Some(ganadores.last) // al que peor le fue en la ultima
    case _ => Some(ganadores.head) // al que mejor le fue en la ultima
  }
}
