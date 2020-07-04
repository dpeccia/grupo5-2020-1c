case class Torneo(postas: List[Posta], dragonesDisponibles: List[Dragon]) {

  // TODO: ver si hay un solo ganador en una posta, y no seguir las demas
  def realizarTorneo(participantes: List[Vikingo], regla: Regla): Option[Vikingo] = {
    postas.foldRight(Option(participantes)) ((posta, resultadoAnterior) => resultadoAnterior match {
      case Some(competidoresPostaAnterior) =>
        val competidoresListos: List[Competidor] =
          prepararseParaPosta(posta, competidoresPostaAnterior, copy().dragonesDisponibles, regla)
        pasanALaSiguientePosta(posta, competidoresListos, regla)
      case None => None
    }) match {
      case ganadores if ganadores.get.length.equals(1) => Some(ganadores.get.head)
      case ganadores => desempatar(ganadores.get, regla)
      case None => None
    }
  }

  // Etapas del torneo

  def prepararseParaPosta(posta: Posta, participantes: List[Vikingo], dragonesDisponiblesPosta: List[Dragon], regla: Regla): List[Competidor] = regla match {
    case BanDeDragones(condicion) => participantes.map(participante => elegirMejorOpcion(participante, dragonesDisponiblesPosta.filter(condicion), posta))
    case Handicap => participantes.reverse.map(participante => elegirMejorOpcion(participante, dragonesDisponiblesPosta, posta))
    case _ => participantes.map(participante => elegirMejorOpcion(participante, dragonesDisponiblesPosta, posta))
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
    case PorEquipos =>  ganadores.groupBy(_.equipo).valuesIterator.maxBy(_.size)
    case _ => Some(ganadores.head) // al que mejor le fue en la ultima
  }

  // Helpers para las etapas del torneo

  private def elegirMejorOpcion(vikingo: Vikingo, dragones: List[Dragon], posta: Posta): Competidor = {
    vikingo.mejorCompetidor(dragones, posta) match {
      case jinete: Jinete =>
        dragones.dropWhile(_.equals(jinete.dragon))
        jinete
      case vikingo: Vikingo => vikingo
    }
  }
}
