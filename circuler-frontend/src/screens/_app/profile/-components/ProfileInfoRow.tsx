import { View, Text } from 'react-native'

type Props = {
  label: string;
  value: string;
  isLast?: boolean;
};

export function ProfileInfoRow({ label, value, isLast = false }: Props) {
  return (
    <View className={`py-4 ${!isLast ? 'border-b border-gray-100' : ''}`}>
      <Text className="text-xs text-gray-400 mb-1 font-medium">{label}</Text>
      <Text className="text-sm text-gray-800">{value}</Text>
    </View>
  )
}
