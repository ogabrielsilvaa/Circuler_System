import { FlatList, TouchableOpacity, Text } from 'react-native'
import { BOOK_CATEGORIES, BookCategory } from '../../../../types/book.types'

type CategoryChipProps = {
  item: BookCategory
  selected: boolean
  onPress: () => void
}

function CategoryChip({ item, selected, onPress }: CategoryChipProps) {
  return (
    <TouchableOpacity
      activeOpacity={0.7}
      onPress={onPress}
      className={
        selected
          ? 'bg-emerald-800 border border-emerald-800 rounded-xl px-4 py-2'
          : 'bg-white border border-gray-200 rounded-xl px-4 py-2'
      }
    >
      <Text className={`text-sm font-medium ${selected ? 'text-white' : 'text-black'}`}>
        {item.label}
      </Text>
    </TouchableOpacity>
  )
}

type CategoryFilterProps = {
  selected: number | null
  onSelect: (code: number | null) => void
}

export function CategoryFilter({ selected, onSelect }: CategoryFilterProps) {
  return (
    <FlatList
      horizontal
      showsHorizontalScrollIndicator={false}
      data={BOOK_CATEGORIES}
      keyExtractor={(item) => String(item.code)}
      renderItem={({ item }) => (
        <CategoryChip
          item={item}
          selected={selected === item.code}
          onPress={() => onSelect(selected === item.code ? null : item.code)}
        />
      )}
      contentContainerStyle={{ gap: 8 }}
    />
  )
}
