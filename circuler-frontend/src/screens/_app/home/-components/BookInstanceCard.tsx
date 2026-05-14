import { View, Text, StyleSheet, TouchableOpacity } from 'react-native'
import { Image } from 'expo-image'
import { BookOpen, MapPin } from 'lucide-react-native'
import { BookInstance } from '../../../../types/book.types'
import { StatusBadge } from '../../../../components/StatusBadge'

// StyleSheet necessário: NativeWind não resolve dimensões fixas dentro de View com overflow-hidden na web
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

type BookInstanceCardProps = { instance: BookInstance; onPress?: () => void }

export function BookInstanceCard({ instance, onPress }: BookInstanceCardProps) {
  return (
    <TouchableOpacity activeOpacity={0.7} onPress={onPress}>
    <View className="bg-white rounded-2xl p-4 shadow flex-row gap-4">
      <View style={styles.thumbnailContainer}>
        {instance.bookThumbnailUrl ? (
          <Image
            source={{ uri: instance.bookThumbnailUrl }}
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
            {instance.bookTitle}
          </Text>
          <StatusBadge status={instance.status} />
        </View>

        <Text className="text-sm text-gray-500">{instance.bookAuthor}</Text>

        <View className="flex-row items-center gap-1 mt-1">
          <MapPin size={12} color="#059669" />
          <Text className="text-xs text-gray-400 flex-1" numberOfLines={1}>
            {instance.collectionPointName}
          </Text>
        </View>
      </View>
    </View>
    </TouchableOpacity>
  )
}
