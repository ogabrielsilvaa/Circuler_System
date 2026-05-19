import { FlatList } from 'react-native'
import { BOOK_CATEGORIES } from '../../../../types/book.types'
import { CategoryChip } from '../../-components/CategoryChip'

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
          label={item.label}
          selected={selected === item.code}
          onPress={() => onSelect(selected === item.code ? null : item.code)}
          variant="light"
        />
      )}
      contentContainerStyle={{ gap: 8 }}
    />
  )
}
