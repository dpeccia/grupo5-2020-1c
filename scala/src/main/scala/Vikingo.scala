trait Competidor {
  def cuantoPuedeCargar: Double
  def danioQueProduce: Double
  def velocidad: Double
  def incrementarNivelDeHambre(hambreAIncrementar: Double)
}

class Vikingo(_peso: Double, _barbarosidad: Double, _item: Item, _velocidad: Double) extends Competidor {
  var peso: Double = _peso
  var barbarosidad: Double = _barbarosidad
  var item: Item = _item
  var nivelDeHambre: Double = 0

  def velocidad: Double = _velocidad

  def cuantoPuedeCargar: Double = peso / 2 + 2 * barbarosidad

  def danioQueProduce: Double = barbarosidad + item.danioQueProduce

  def incrementarNivelDeHambre(hambreAIncrementar: Double) = nivelDeHambre += hambreAIncrementar

  def montar(dragon: Dragon): Jinete = {
    if(dragon.puedeSerMontadoPor(this)) {
      return new Jinete(this, dragon)
    }
    throw new Exception
  }

}

class Jinete(_vikingo: Vikingo, _dragon: Dragon) extends Competidor {
  private val vikingo = _vikingo
  private val dragon = _dragon

  def cuantoPuedeCargar: Double = vikingo.peso - dragon.cuantoPuedeCargar

  def danioQueProduce: Double = vikingo.danioQueProduce + dragon.danio

  def velocidad: Double = (dragon.velocidadDeVuelo - vikingo.peso).max(0)

  def incrementarNivelDeHambre(hambreAIncrementar: Double) = vikingo.incrementarNivelDeHambre(vikingo.nivelDeHambre * 0.05)
}
