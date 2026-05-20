import { View, Text, FlatList, ActivityIndicator } from 'react-native'
import { useLocalSearchParams, useRouter } from 'expo-router'
import { MapPin } from 'lucide-react-native'
import { useCollectionPointDetail } from '../../../hooks/useCollectionPoints'
import { CollectionPointBookCard } from './-components/CollectionPointBookCard'

export default function CollectionPointDetail() {
  const { id } = useLocalSearchParams<{ id: string }>()
  const router = useRouter()
  const { collectionPoint: detail, isLoading, isError } = useCollectionPointDetail(Number(id))

  if (isLoading) {
    return (
      <View className="flex-1 items-center justify-center bg-gray-100">
        <ActivityIndicator size="large" color="#059669" />
      </View>
    )
  }

  if (isError || !detail) {
    return (
      <View className="flex-1 items-center justify-center bg-gray-100 px-6">
        <Text className="text-base text-gray-500 text-center">
          Não foi possível carregar o ponto de coleta.
        </Text>
      </View>
    )
  }

  const books = detail.books ?? []

  return (
    <View className="flex-1 bg-gray-100">
      <View className="bg-emerald-800 px-4 py-5 gap-2">
        <Text className="text-lg font-bold text-white" numberOfLines={2}>
          {detail.name}
        </Text>
        <View className="flex-row items-center gap-1">
          <MapPin size={14} color="#a7f3d0" />
          <Text className="text-sm text-emerald-200" numberOfLines={1}>
            {detail.addressStreet} - {detail.addressNeighborhood}
          </Text>
        </View>
      </View>

      {books.length === 0 ? (
        <View className="flex-1 items-center justify-center px-6">
          <Text className="text-base text-gray-500 text-center">
            Nenhum exemplar disponível neste ponto de coleta.
          </Text>
        </View>
      ) : (
        <FlatList
          data={books}
          keyExtractor={(item) => String(item.id)}
          renderItem={({ item }) => (
            <CollectionPointBookCard
              book={item}
              onPress={() => router.push(`/books/${item.id}`)}
            />
          )}
          ItemSeparatorComponent={() => <View className="h-3" />}
          contentContainerStyle={{ padding: 16 }}
        />
      )}
    </View>
  )
}
