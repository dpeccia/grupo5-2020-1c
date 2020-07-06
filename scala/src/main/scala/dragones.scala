trait Dragon {
  val peso: Int
  val danio: Int
  val requisitosEspecificos: RequisitoDragon

  def requisitoBase: RequisitoDragon = RequisitoPeso

  def cumpleRequisitosBaseYEspecificos(vikingo: Vikingo): Boolean = requisitoBase.apply(this,vikingo) && requisitosEspecificos.apply(this, vikingo)

  def velocidadBase: Double = 60

  def velocidadDeVuelo: Double = velocidadBase - peso

  def cuantoPuedeCargar: Double = 0.2 * peso

  def puedeSerMontadoPor(vikingo: Vikingo): Boolean = cumpleRequisitosBaseYEspecificos(vikingo)
}

case class FuriaNocturna (peso: Int,danio: Int, item: Item) extends Dragon() {
  override val requisitosEspecificos: RequisitoDragon = RequisitoItemParticular(item)
  override def velocidadDeVuelo: Double = super.velocidadDeVuelo * 3
}

case class NadderMortifero (peso: Int) extends Dragon() {
  override val requisitosEspecificos: RequisitoDragon = RequisitoDanio
  override val danio: Int = 150
}

case class Gronckle (peso: Int, pesoDeterminado: Int) extends Dragon() {
  override val requisitosEspecificos: RequisitoDragon = RequisitoPesoDeterminadoDragon(pesoDeterminado)
  override val danio: Int = peso * 5
  override def velocidadBase: Double = super.velocidadBase / 2
}


