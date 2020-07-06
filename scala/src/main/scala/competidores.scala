trait Competidor {
  def puedeParticipar(posta: Posta): Boolean
  def tieneArma: Boolean
  def cuantoPuedeCargar: Double
  val danioQueProduce: Double
  def velocidad: Double
  def incrementarNivelDeHambre(hambreAIncrementar: Double): Competidor
  def barbarosidad: Double
  def nivelDeHambre: Double
  def esMejorQue(otroCompetidor: Competidor)(posta: Posta): Boolean = posta.esMejor(this, otroCompetidor)
  def vikingoAsociado: Vikingo
  def tenesDragon: Boolean
  def esTuDragon(unDragon: Dragon): Boolean = false
}

case class Vikingo(peso: Double, barbarosidad: Double, item: Item, velocidad: Double, nivelDeHambre: Double = 0) extends Competidor {
  val danioQueProduce: Double = barbarosidad + item.danioQueProduce

  def tieneArma: Boolean = item.esArma()

  def tieneItem(unItem: Item): Boolean = item.equals(unItem)

  def cuantoPuedeCargar: Double = peso / 2 + 2 * barbarosidad

  def puedeParticipar(posta: Posta): Boolean = {
    val unCompetidor: Competidor = this.incrementarNivelDeHambre(posta.nivelDeHambreQueIncrementa)
    unCompetidor.nivelDeHambre < 100
  }

  def dragonesQuePuedeMontar(dragones: List[Dragon]): List[Competidor] = dragones.flatMap(dragon => montar(dragon))

  def mejorCompetidor(dragones: List[Dragon], posta: Posta): Competidor = this.dragonesQuePuedeMontar(dragones) match {
    case opciones if opciones.isEmpty => this
    case opciones => opciones.maxBy(esMejorQue (_)(posta))
  }

  def incrementarNivelDeHambre(hambreAIncrementar: Double): Vikingo = copy(nivelDeHambre = nivelDeHambre + hambreAIncrementar)

  def montar(dragon: Dragon): Option[Jinete] = {
    if(dragon.puedeSerMontadoPor(this)) {
      return Some(Jinete(this, dragon))
    }
    None
  }

  def vikingoAsociado: Vikingo = this

  def tenesDragon: Boolean = false

  def esDeEquipo(equipo: Equipo): Boolean = equipo.vikingos.contains(this)
}

case class Jinete(vikingo: Vikingo, dragon: Dragon) extends Competidor {
  def nivelDeHambre: Double = vikingo.nivelDeHambre

  def puedeParticipar(posta: Posta): Boolean = vikingo.puedeParticipar(posta)

  def cuantoPuedeCargar: Double = vikingo.peso - dragon.cuantoPuedeCargar

  val danioQueProduce: Double = vikingo.danioQueProduce + dragon.danio

  def velocidad: Double = (dragon.velocidadDeVuelo - vikingo.peso).max(0)

  def incrementarNivelDeHambre(hambreAIncrementar: Double): Jinete = copy(vikingo = vikingo.incrementarNivelDeHambre(5))

  def barbarosidad: Double = vikingo.barbarosidad

  def tieneArma: Boolean = vikingo.tieneArma

  def vikingoAsociado: Vikingo = vikingo

  def dragonAsociado: Option[Dragon] = Some(dragon)

  def tenesDragon: Boolean = true

  override def esTuDragon(unDragon: Dragon): Boolean = unDragon.equals(dragon)
}

object Patapez extends Vikingo(10,10, Comestible(10), 10) {
  
  override def puedeParticipar(posta: Posta): Boolean = nivelDeHambre < 50

  override def incrementarNivelDeHambre(hambreAIncrementar: Double): Vikingo = super.incrementarNivelDeHambre(hambreAIncrementar*2)
}
