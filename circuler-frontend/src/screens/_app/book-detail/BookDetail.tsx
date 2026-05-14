import { ScrollView, View, Text, StyleSheet, ActivityIndicator } from 'react-native'
import { Image } from 'expo-image'
import { BookOpen, MapPin } from 'lucide-react-native'
import { useLocalSearchParams } from 'expo-router'
import { BOOK_CATEGORIES } from '../../../types/book.types'
import { useBookInstanceById } from '../../../hooks/useBookInstances'

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

export default function BookDetail() {
  const { id } = useLocalSearchParams<{ id: string }>()
  const { data: instance, isLoading, isError } = useBookInstanceById(Number(id))

  if (isLoading) {
    return (
      <View className="flex-1 items-center justify-center bg-gray-100">
        <ActivityIndicator size="large" color="#059669" />
      </View>
    )
  }

  if (isError || !instance) {
    return (
      <View className="flex-1 items-center justify-center bg-gray-100 px-4">
        <Text className="text-base text-gray-500 text-center">
          Não foi possível carregar o exemplar.
        </Text>
      </View>
    )
  }

  const categoryLabel =
    BOOK_CATEGORIES.find((c) => c.key === instance.bookCategory)?.label ?? instance.bookCategory

  return (
    <ScrollView className="flex-1 bg-gray-100" contentContainerStyle={{ paddingBottom: 32 }}>
      <View style={styles.coverContainer}>
        {instance.bookThumbnailUrl ? (
          <Image source={{ uri: instance.bookThumbnailUrl }} style={styles.cover} contentFit="cover" />
        ) : (
          <BookOpen size={64} color="#047857" />
        )}
      </View>

      <View className="px-4 py-5 gap-3">
        <View className="bg-emerald-500 self-start rounded-full px-3 py-1">
          <Text className="text-xs text-emerald-800">{categoryLabel}</Text>
        </View>

        <Text className="text-xl font-bold text-black">{instance.bookTitle}</Text>

        <Text className="text-sm text-gray-500">{instance.bookAuthor}</Text>

        <View className="h-px bg-gray-200 my-4" />

        <View>
          <View className="flex-row items-center gap-2">
            <MapPin size={16} color="#059669" />
            <Text className="text-base font-bold text-black">Estoque Físico Disponível</Text>
          </View>

          <View className="bg-white rounded-2xl p-4 shadow mt-3">
            <Text className="text-base font-bold text-black">{instance.collectionPointName}</Text>
          </View>
        </View>
      </View>
    </ScrollView>
  )
}
