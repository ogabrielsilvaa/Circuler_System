import { TouchableOpacity, Text } from 'react-native'

type Props = {
  label: string
  selected: boolean
  onPress: () => void
  variant?: 'dark' | 'light'
}

export function CategoryChip({ label, selected, onPress, variant = 'dark' }: Props) {
  const containerClass =
    variant === 'light'
      ? selected
        ? 'bg-emerald-800 border border-emerald-800 rounded-xl px-4 py-2'
        : 'bg-white border border-gray-200 rounded-xl px-4 py-2'
      : `rounded-full px-4 py-2 ${selected ? 'bg-emerald-500' : 'bg-white/20'}`

  const textClass =
    variant === 'light'
      ? `text-sm font-medium ${selected ? 'text-white' : 'text-black'}`
      : 'text-white text-sm font-medium'

  return (
    <TouchableOpacity
      activeOpacity={0.7}
      onPress={onPress}
      className={`mr-2 ${containerClass}`}
    >
      <Text className={textClass}>{label}</Text>
    </TouchableOpacity>
  )
}
