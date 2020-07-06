case class Equipo(vikingos: List[Vikingo]) {
  def cantidadDeGanadores(ganadoresDelTorneo: List[Vikingo]): Int = ganadoresDelTorneo.count(_.esDeEquipo(this))
}

case class Torneo(postas: List[Posta], dragonesDisponibles: List[Dragon],regla: Regla) {

  def realizarTorneoIndividualmente(participantes: List[Vikingo]): Option[Vikingo] = {
    val ganadores = obtenerGanadores(participantes).getOrElse(return None)
    if (ganadores.length.equals(1)) return Some(ganadores.head)
    desempatar(ganadores)
  }
  def setRegla(nuevaRegla: Regla): Torneo = {
    copy(regla = nuevaRegla)
  }


  def realizarTorneoPorEquipos(participantes: List[Equipo]): Option[Equipo] = {
    val ganadores = obtenerGanadores(participantes.flatMap(_.vikingos)).getOrElse(return None)
    if (ganadores.length.equals(1)) return Some(Equipo(ganadores))
    Some(participantes.maxBy(_.cantidadDeGanadores(ganadores)))
  }

  def obtenerGanadores(participantes: List[Vikingo]): Option[List[Vikingo]] = {
    postas.foldRight(Option(participantes))((posta, resultadoAnterior) => {
      val ganadoresPostaAnterior = prepararseParaPostaSegunRegla(posta, resultadoAnterior.get, dragonesDisponibles)
      val resultadoPosta = pasanALaSiguientePosta(posta, ganadoresPostaAnterior)
      resultadoPosta match {
        case Some(ganadores) if ganadores.length.equals(1) => return Some(ganadores)
        case Some(ganadores) => Some(ganadores)
        case _ => return None
      }
    })
  }

  // Etapas del torneo

  def prepararseParaPostaSegunRegla(posta: Posta, participantes: List[Vikingo], dragonesDisponiblesPosta: List[Dragon]): List[Competidor] = {
    regla.prepararseParaPosta(posta,participantes,dragonesDisponibles)
  }

  def pasanALaSiguientePosta(posta: Posta, participantes: List[Competidor]): Option[List[Vikingo]] = {
    regla.pasanALaSiguientePosta(posta,participantes)
  }

  def desempatar(ganadores: List[Vikingo]): Option[Vikingo] = {
    regla.desempatar(ganadores)
  }
}
