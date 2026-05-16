import { View, Text, StyleSheet, TouchableOpacity } from 'react-native'
import { Image } from 'expo-image'
import { BookOpen } from 'lucide-react-native'
import { CollectionPointBook } from '../../../../types/collection-point.types'
import { StatusBadge } from '../../../../components/StatusBadge'

const styles = StyleSheet.create({
  thumbnailContainer: {
    width: 80,
    height: 112,
    borderRadius: 12,
    backgroundColor: '#10b981',
    overflow: 'hidden',
    alignItems: 'center',
    justifyContent: 'center',
  },
  thumbnail: { flex: 1, width: '100%' },
})

type CollectionPointBookCardProps = { book: CollectionPointBook; onPress?: () => void }

export function CollectionPointBookCard({ book, onPress }: CollectionPointBookCardProps) {
  return (
    <TouchableOpacity activeOpacity={0.7} onPress={onPress}>
      <View className="bg-white rounded-2xl p-4 shadow flex-row gap-4">
        <View style={styles.thumbnailContainer}>
          {book.bookThumbnailUrl ? (
            <Image
              source={{ uri: book.bookThumbnailUrl }}
              style={styles.thumbnail}
              contentFit="cover"
            />
          ) : (
            <BookOpen size={32} color="#047857" />
          )}
        </View>

        <View className="flex-1 gap-1">
          <View className="flex-row items-start justify-between gap-2">
            <Text className="text-base font-bold text-black flex-1" numberOfLines={2}>
              {book.bookTitle}
            </Text>
            <StatusBadge status={book.status} />
          </View>

          <Text className="text-sm text-gray-500">{book.bookAuthor}</Text>
        </View>
      </View>
    </TouchableOpacity>
  )
}
