require_relative '../lib/trait'

describe "Tests de Traits" do

  before :all do
    Trait.define do
      name :MiTrait
      method :metodo1 do
        "Hola"
      end
      method :metodo2 do |un_numero|
        un_numero * 0 + 42
      end
    end

    class MiClase
      uses MiTrait
      def metodo1
        "mundo"
      end
    end

    Trait.define do
      name :MiOtroTrait
      method :metodo1 do
        "Chau"
      end
      method :metodo3 do
        20
      end
    end

    Trait.define do
      name :MiOtroTrait2
      method :metodo4 do
        "chau"
      end
      method :metodo3 do
        10
      end
    end

    class Conflicto
      #uses MiTrait + MiOtroTrait
    end

    class Suma
      uses MiTrait + MiOtroTrait2
    end

    class B
      uses MiTrait - :metodo1
    end

    end

  describe 'Definicion de Trait' do
    it 'se creo el trait con su nombre' do
      expect{Object.const_get(MiTrait.nombre)}.not_to raise_exception
    end
    it 'se guardo el nombre que le definimos' do
      expect(MiTrait.nombre).to be :MiTrait
    end
    it 'tiene los metodos que le definimos' do
      expect(MiTrait.metodos.map{|m| m.nombre}).to eq [:metodo1,:metodo2]
    end
    it 'el :metodo2 solo se creo para esa instancia' do
      expect(MiTrait.metodos.map{|m| m.nombre}).to include :metodo2
    end
  end

  describe 'Aplicacion de Trait' do
    it 'MiClase tiene el :metodo2 de MiTrait' do
      expect(MiClase.instance_methods).to include :metodo2
    end
    it 'Mi Clase devuelve el metodo'do
      expect(MiClase.new.metodo2 2).to eq 42
    end
    it 'no se pisa el metodo que ya tenia la clase' do
      expect(MiClase.new.metodo1).to eq "mundo"
    end
  end

  describe 'Suma de Traits' do
    it 'tiene los metodos de ambos traits' do
      expect(Suma.instance_methods).to include :metodo1,:metodo2, :metodo3, :metodo4
    end
  end

  describe 'Resta de Traits' do
    it 'se remueve el metodo especificado' do
      expect(B.instance_methods).not_to include :metodo1
    end
    it 'tira error si se remueve un metodo inexistente' do
      expect{class C uses MiTrait - :metodo8 end}.to raise_error "Solo remueve metodos incluidos en su trait"
    end
  end

  describe "Resolucion de conflictos" do
    it 'la estrategia de orden de aparicion aplica ambos metodos' do
      class C
        uses (MiTrait + MiOtroTrait) & OrdenDeAparicion.new
      end
      expect(C.new.metodo1).to eq "Hola" && "Chau"
    end
    it 'la estrategia de aplicar funcion ejecuta ambas como un fold' do
      class D
        uses (MiOtroTrait + MiOtroTrait2) & AplicarFuncion.new(:*)
      end
      expect(D.new.metodo3).to eq 200
    end
    it 'la estrategia de aplicar condicion corta el flujo si la cumple' do
      class E
        uses (MiOtroTrait + MiOtroTrait2) & AplicarPorCondicion.new(proc {|m| m>8})
      end
      expect(E.new.metodo3).to eq 20
    end
  end

end