import { View, Text, FlatList, TouchableOpacity } from 'react-native'
import { useRouter } from 'expo-router'
import { BookInstance } from '../../../../types/book.types'
import { BookInstanceCard } from './BookInstanceCard'

type BookInstanceListProps = { instances: BookInstance[] }

export function BookInstanceList({ instances }: BookInstanceListProps) {
  const router = useRouter()

  return (
    <View>
      <View className="flex-row justify-between items-center mb-4">
        <Text className="text-base font-bold text-black">Exemplares disponíveis</Text>
      </View>

      <FlatList
        data={instances}
        keyExtractor={(item) => String(item.id)}
        renderItem={({ item }) => (
          <BookInstanceCard
            instance={item}
            onPress={() => router.push(`/books/${item.id}`)}
          />
        )}
        ItemSeparatorComponent={() => <View className="h-3" />}
        scrollEnabled={false}
        contentContainerStyle={{ padding: 12, gap: 12 }}
      />
    </View>
  )
}
