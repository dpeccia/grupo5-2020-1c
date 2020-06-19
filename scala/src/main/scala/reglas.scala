trait ReglaTorneo {
  def prepararseParaPosta(vikingos: List[Vikingo], dragones: List[Dragon], posta: Posta): List[Competidor]
  def pasanALaSiguientePosta(participantes: List[Competidor], posta: Posta): List[Vikingo]
  def desempatar(posiblesGanadores: List[Vikingo]): Vikingo
}

case class Estandar() extends ReglaTorneo {
  def prepararseParaPosta(vikingos: List[Vikingo], dragones: List[Dragon], posta: Posta): List[Competidor] = {
    var listaDeDragones: List[Dragon] = dragones.clone().asInstanceOf[List[Dragon]]
    vikingos.map(vikingo => vikingo.mejorMontura(dragones, posta) match {
      case jinete: Jinete =>
        listaDeDragones.dropWhile(_.equals(jinete.dragon))
        jinete
      case vikingo: Vikingo => vikingo
    })
  }

  def pasanALaSiguientePosta(participantes: List[Competidor], posta: Posta): List[Vikingo] = {
    val competidores = posta.realizarPosta(participantes)
    competidores.take(competidores.length / 2).map {
      case jinete: Jinete => jinete.vikingo
      case vikingo: Vikingo => vikingo
    }
  }

  def desempatar(posiblesGanadores: List[Vikingo]): Vikingo = posiblesGanadores.head
}

case class Eliminacion(cantidadAEliminar: Int) extends Estandar {
  override def pasanALaSiguientePosta(participantes: List[Competidor], posta: Posta): List[Vikingo] = {
    val competidores = posta.realizarPosta(participantes)
    competidores.take(competidores.length - cantidadAEliminar).map {
      case jinete: Jinete => jinete.vikingo
      case vikingo: Vikingo => vikingo
    }
  }
}

case class TorneoInverso() extends Estandar {
  override def pasanALaSiguientePosta(participantes: List[Competidor], posta: Posta): List[Vikingo] = {
    val competidores = posta.realizarPosta(participantes)
    competidores.takeRight(competidores.length / 2).map {
      case jinete: Jinete => jinete.vikingo
      case vikingo: Vikingo => vikingo
    }
  }

  override def desempatar(posiblesGanadores: List[Vikingo]): Vikingo = posiblesGanadores.last
}

case class BanDeDragones(condicion: Dragon=>Boolean) extends Estandar {
  override def prepararseParaPosta(vikingos: List[Vikingo], dragones: List[Dragon], posta: Posta): List[Competidor] = {
    var listaDeDragones: List[Dragon] = dragones.clone().asInstanceOf[List[Dragon]].filter(condicion)
    vikingos.map(vikingo => vikingo.mejorMontura(dragones, posta) match {
      case jinete: Jinete =>
        listaDeDragones.dropWhile(_.equals(jinete.dragon))
        jinete
      case vikingo: Vikingo => vikingo
    })
  }
}

case class Handicap() extends Estandar {
  override def prepararseParaPosta(vikingos: List[Vikingo], dragones: List[Dragon], posta: Posta): List[Competidor] = {
    var listaDeDragones: List[Dragon] = dragones.clone().asInstanceOf[List[Dragon]]
    vikingos.reverse.map(vikingo => vikingo.mejorMontura(dragones, posta) match {
      case jinete: Jinete =>
        listaDeDragones.dropWhile(_.equals(jinete.dragon))
        jinete
      case vikingo: Vikingo => vikingo
    })
  }
}
