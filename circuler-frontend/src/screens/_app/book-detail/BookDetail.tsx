import { ScrollView, View, Text, StyleSheet } from 'react-native'
import { Image } from 'expo-image'
import { BookOpen, MapPin } from 'lucide-react-native'
import { BookCategoryEnum, BookInstanceStatus } from '../../../constants/enums'
import { BOOK_CATEGORIES } from '../../../types/book.types'

// StyleSheet necessário: NativeWind não resolve dimensões fixas com overflow-hidden na web
const styles = StyleSheet.create({
  coverContainer: {
    width: '100%',
    height: 280,
    backgroundColor: '#10b981',
    alignItems: 'center',
    justifyContent: 'center',
  },
  cover: { flex: 1, width: '100%' },
})

const MOCK_DETAIL = {
  id: 1,
  bookId: 1,
  bookTitle: 'O Cortiço',
  bookAuthor: 'Aluísio Azevedo',
  bookIsbn: '978-8535902776',
  bookCategory: 'DIDATICO' as BookCategoryEnum,
  bookDescription:
    'Um clássico da literatura brasileira que retrata a vida em um cortiço no Rio de Janeiro do século XIX, explorando as relações sociais, os conflitos de classe e a luta pela sobrevivência em um ambiente coletivo e denso.',
  status: BookInstanceStatus.DISPONIVEL,
  collectionPointId: 1,
  collectionPointName: 'Ponto Duque de Caxias Centro',
  collectionPointAddress: 'Av. Presidente Vargas, 250 - Centro, Duque de Caxias',
  collectionPointOwnerName: 'João Silva',
  bookThumbnailUrl: null as string | null,
  userDonorId: null as number | null,
  userDonorName: null as string | null,
  createdAt: '2025-01-10T10:00:00',
  updatedAt: '2025-01-10T10:00:00',
}

export default function BookDetail() {
  const detail = MOCK_DETAIL
  const categoryLabel =
    BOOK_CATEGORIES.find((c) => c.key === detail.bookCategory)?.label ?? detail.bookCategory

  return (
    <ScrollView className="flex-1 bg-gray-100" contentContainerStyle={{ paddingBottom: 32 }}>
      <View style={styles.coverContainer}>
        {detail.bookThumbnailUrl ? (
          <Image source={{ uri: detail.bookThumbnailUrl }} style={styles.cover} contentFit="cover" />
        ) : (
          <BookOpen size={64} color="#047857" />
        )}
      </View>

      <View className="px-4 py-5 gap-3">
        <View className="bg-emerald-500 self-start rounded-full px-3 py-1">
          <Text className="text-xs text-emerald-800">{categoryLabel}</Text>
        </View>

        <Text className="text-xl font-bold text-black">{detail.bookTitle}</Text>

        <Text className="text-sm text-gray-500">{detail.bookAuthor}</Text>

        <View className="mt-2">
          <Text className="text-base font-bold text-black">Sobre a obra</Text>
          <Text className="text-sm text-gray-600 mt-1 leading-relaxed">{detail.bookDescription}</Text>
        </View>

        <View className="h-px bg-gray-200 my-4" />

        <View>
          <View className="flex-row items-center gap-2">
            <MapPin size={16} color="#059669" />
            <Text className="text-base font-bold text-black">Estoque Físico Disponível</Text>
          </View>

          <View className="bg-white rounded-2xl p-4 shadow mt-3">
            <Text className="text-base font-bold text-black">{detail.collectionPointName}</Text>
            <Text className="text-sm text-gray-500 mt-1">{detail.collectionPointAddress}</Text>
            <Text className="text-sm text-gray-400 mt-0.5">{detail.collectionPointOwnerName}</Text>
          </View>
        </View>
      </View>
    </ScrollView>
  )
}
