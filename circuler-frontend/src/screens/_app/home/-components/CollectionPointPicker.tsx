import { View, Text, TouchableOpacity, ActivityIndicator } from 'react-native'
import { CollectionPointStatus } from '../../../../constants/enums'
import { CollectionPoint } from '../../../../types/collection-point.types'
import { useCollectionPoints } from '../../../../hooks/useCollectionPoints'

type CollectionPointPickerProps = {
  value: number | null
  onChange: (id: number) => void
  error?: string
}

function StatusChip({ status }: { status: CollectionPointStatus }) {
  if (status === CollectionPointStatus.LOTADO) {
    return (
      <View className="bg-amber-100 rounded-full px-2 py-0.5">
        <Text className="text-xs font-medium text-amber-700">Lotado</Text>
      </View>
    )
  }
  return (
    <View className="bg-emerald-100 rounded-full px-2 py-0.5">
      <Text className="text-xs font-medium text-emerald-800">Disponível</Text>
    </View>
  )
}

function PointItem({
  point,
  selected,
  onPress,
}: {
  point: CollectionPoint
  selected: boolean
  onPress: () => void
}) {
  return (
    <TouchableOpacity activeOpacity={0.7} onPress={onPress}>
      <View
        className={`flex-row items-center justify-between rounded-xl px-4 py-3 mb-2 ${
          selected ? 'border-2 border-emerald-400 bg-white/20' : 'bg-white/10'
        }`}
      >
        <View className="flex-1 pr-3">
          <Text className="text-sm font-medium text-white" numberOfLines={1}>
            {point.name}
          </Text>
          <Text className="text-xs text-emerald-200 mt-0.5" numberOfLines={1}>
            {point.addressNeighborhood}
          </Text>
        </View>
        <StatusChip status={point.status} />
      </View>
    </TouchableOpacity>
  )
}

export function CollectionPointPicker({ value, onChange, error }: CollectionPointPickerProps) {
  const { collectionPoints, isLoading } = useCollectionPoints()

  const visiblePoints = collectionPoints.filter(
    (p) => p.status !== CollectionPointStatus.APAGADO && p.status !== CollectionPointStatus.INATIVO,
  )

  return (
    <View className="mb-4">
      <Text className="text-sm font-medium text-white mb-1">Ponto de Coleta *</Text>

      {isLoading ? (
        <ActivityIndicator size="small" color="#a7f3d0" />
      ) : (
        visiblePoints.map((point) => (
          <PointItem
            key={point.id}
            point={point}
            selected={value === point.id}
            onPress={() => onChange(point.id)}
          />
        ))
      )}

      {error ? <Text className="text-red-400 text-xs mt-1">{error}</Text> : null}
    </View>
  )
}
