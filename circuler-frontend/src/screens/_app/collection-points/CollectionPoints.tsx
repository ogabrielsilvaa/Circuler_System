import { View, Text, FlatList, ActivityIndicator } from 'react-native'
import { useRouter } from 'expo-router'
import { useCollectionPoints } from '../../../hooks/useCollectionPoints'
import { CollectionPointCard } from './-components/CollectionPointCard'

export default function CollectionPoints() {
  const router = useRouter()
  const { collectionPoints, isLoading, isError } = useCollectionPoints()

  if (isLoading) {
    return (
      <View className="flex-1 items-center justify-center bg-gray-100">
        <ActivityIndicator size="large" color="#059669" />
      </View>
    )
  }

  if (isError) {
    return (
      <View className="flex-1 items-center justify-center bg-gray-100 px-6">
        <Text className="text-base text-gray-500 text-center">
          Não foi possível carregar os pontos de coleta.
        </Text>
      </View>
    )
  }

  return (
    <View className="flex-1 bg-gray-100">
      {collectionPoints.length === 0 ? (
        <View className="flex-1 items-center justify-center px-6">
          <Text className="text-base text-gray-500 text-center">
            Nenhum ponto de coleta disponível.
          </Text>
        </View>
      ) : (
        <FlatList
          data={collectionPoints}
          keyExtractor={(item) => String(item.id)}
          renderItem={({ item }) => (
            <CollectionPointCard
              point={item}
              onPress={() => router.push(`/collection-points/${item.id}`)}
            />
          )}
          ItemSeparatorComponent={() => <View className="h-3" />}
          contentContainerStyle={{ padding: 16 }}
        />
      )}
    </View>
  )
}
