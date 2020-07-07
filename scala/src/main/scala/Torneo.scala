case class Equipo(vikingos: List[Vikingo]) {
  def cantidadDeGanadores(ganadoresDelTorneo: List[Vikingo]): Int = ganadoresDelTorneo.count(_.esDeEquipo(this))
}

case class Torneo(postas: List[Posta], dragonesDisponibles: List[Dragon]) {

  def realizarTorneoIndividualmente(participantes: List[Vikingo], regla: Regla): Option[Vikingo] = {
    val ganadores = obtenerGanadores(participantes, regla).getOrElse(return None)
    regla.desempatar(ganadores)
  }

  def realizarTorneoPorEquipos(participantes: List[Equipo]): Option[Equipo] = {
    val ganadores = obtenerGanadores(participantes.flatMap(_.vikingos), new Estandar).getOrElse(return None)
    if (ganadores.length.equals(1)) return Some(Equipo(ganadores))
    Some(participantes.maxBy(_.cantidadDeGanadores(ganadores)))
  }

  def obtenerGanadores(participantes: List[Vikingo], regla: Regla): Option[List[Vikingo]] = {
    postas.foldLeft(Option(participantes))((resultadoAnterior, posta) => {
      val competidoresPreparados = regla.prepararseParaPosta(posta, resultadoAnterior.get, dragonesDisponibles)
      val resultadoPosta = regla.pasanALaSiguientePosta(posta, competidoresPreparados)
      if (resultadoPosta.getOrElse(return None).length.equals(1)) return resultadoPosta
      resultadoPosta
    })
  }
}
