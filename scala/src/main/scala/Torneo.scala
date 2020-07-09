case class Equipo(vikingos: List[Vikingo]) {
  def cantidadDeGanadores(ganadoresDelTorneo: List[Vikingo]): Int = ganadoresDelTorneo.count(_.esDeEquipo(this))
}

case class Torneo(postas: List[Posta], dragonesDisponibles: List[Dragon]) {

  def realizarTorneoIndividualmente(participantes: List[Vikingo], regla: Regla): Option[Vikingo] = {
    regla.desempatar(obtenerGanadores(participantes, regla))
  }

  def realizarTorneoPorEquipos(participantes: List[Equipo]): Option[Equipo] = {
    desempatarEquipos(participantes,obtenerGanadores(participantes.flatMap(_.vikingos), new Estandar))
    //val ganadores = obtenerGanadores(participantes.flatMap(_.vikingos), new Estandar).getOrElse(return None)
    //if (ganadores.length.equals(1)) return Some(Equipo(ganadores))
    //Some(participantes.maxBy(_.cantidadDeGanadores(ganadores)))
  }
  def desempatarEquipos(participantes: List[Equipo],ganadores: List[Vikingo]):Option[Equipo] = if (ganadores.length.equals(1)) Some(Equipo(ganadores)) else Some(participantes.maxBy(_.cantidadDeGanadores(ganadores)))

  def obtenerGanadores(participantes: List[Vikingo], regla: Regla): List[Vikingo] = {
    postas.foldLeft(participantes)((resultadoAnterior, posta) => {
      val competidoresPreparados = regla.prepararseParaPosta(posta, resultadoAnterior, dragonesDisponibles)
      val resultadoPosta = regla.pasanALaSiguientePosta(posta, competidoresPreparados)
      if (resultadoPosta.length.equals(1)) return resultadoPosta
      resultadoPosta
    })
  }
}
