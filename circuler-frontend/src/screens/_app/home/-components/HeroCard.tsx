import { View, Text, TextInput } from 'react-native'
import { Search } from 'lucide-react-native'

interface HeroCardProps {
  searchQuery: string
  onSearchChange: (text: string) => void
}

export function HeroCard({ searchQuery, onSearchChange }: HeroCardProps) {
  return (
    <View className="bg-white rounded-2xl p-5 shadow-2xl">
      <Text className="text-2xl font-bold text-black">
        Democratizando a leitura na{' '}
        <Text className="text-emerald-600">Baixada Fluminense</Text>
      </Text>

      <Text className="text-sm text-gray-500 mt-2">
        Encontre livros didáticos e literatura perto de você. Reserve online e retire gratuitamente.
      </Text>

      <View className="flex-row items-center bg-gray-100 rounded-xl px-3 mt-4">
        <Search size={16} color="#6b7280" />
        <TextInput
          className="flex-1 py-3 pl-2 text-sm text-black"
          placeholder="Buscar livro..."
          placeholderTextColor="#9ca3af"
          value={searchQuery}
          onChangeText={onSearchChange}
        />
      </View>
    </View>
  )
}
