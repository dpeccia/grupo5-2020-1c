class Torneo(var _postas: List[Posta],var _dragonesDisponibles: List[Dragon]) {
  var postas: List[Posta] = _postas
  var dragonesDisponibles: List[Dragon] = _dragonesDisponibles

  def realizarTorneo(participantes: List[Vikingo], regla: Regla): Option[Vikingo] = {
    postas.foldRight(Option(participantes)) ((posta, resultadoAnterior) => resultadoAnterior match {
      case Some(competidoresPostaAnterior) =>
        val competidoresListos: List[Competidor] =
          prepararseParaPosta(posta, competidoresPostaAnterior, dragonesDisponibles.clone().asInstanceOf[List[Dragon]], regla)
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
    case Eliminacion(cantidadAEliminar) => posta.realizarPosta(participantes) match {
      case Some(ganadoresPosta) => Some(ganadoresPosta.take(cantidadAEliminar))
      case None => None
    } // todos menos los ultimos "cantidadAEliminar"
    case TorneoInverso => posta.realizarPosta(participantes) match {
      case Some(ganadoresPosta) => Some(ganadoresPosta.takeRight(ganadoresPosta.length / 2))
      case None => None
    } // la mitad de los perdedores
    case _ => posta.realizarPosta(participantes) match {
      case Some(ganadoresPosta) => Some(ganadoresPosta.take(ganadoresPosta.length / 2))
      case None => None
    } // la mitad de los ganadores
  }

  def desempatar(ganadores: List[Vikingo], regla: Regla): Option[Vikingo] = regla match {
    case TorneoInverso => Some(ganadores.last) // al que peor le fue en la ultima
    case _ => Some(ganadores.head) // al que mejor le fue en la ultima
  }

  // Helpers para las etapas del torneo

  private def elegirMejorOpcion(vikingo: Vikingo, dragones: List[Dragon], posta: Posta): Competidor = {
    vikingo.mejorMontura(dragones, posta) match {
      case jinete: Jinete =>
        dragones.dropWhile(_.equals(jinete.dragon))
        jinete
      case vikingo: Vikingo => vikingo
    }
  }
}
