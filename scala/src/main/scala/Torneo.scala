case class Equipo(vikingos: List[Vikingo]) {
  def cantidadDeGanadores(ganadoresDelTorneo: List[Vikingo]): Int = ganadoresDelTorneo.count(_.esDeEquipo(this))
}

case class Torneo(postas: List[Posta], dragonesDisponibles: List[Dragon],regla: Regla) {

  def realizarTorneoIndividualmente(participantes: List[Vikingo]): Option[Vikingo] = {
    val ganadores = obtenerGanadores(participantes).getOrElse(return None)
    if (ganadores.length.equals(1)) return Some(ganadores.head)
    regla.desempatar(ganadores)//regla.desempatar(participantes)
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
      val ganadoresPostaAnterior = regla.prepararseParaPosta(posta,resultadoAnterior.get,dragonesDisponibles)
      regla.pasanALaSiguientePosta(posta, ganadoresPostaAnterior)
    })
  }
}
