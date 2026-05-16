import { View, Text, TouchableOpacity } from 'react-native'
import { useSafeAreaInsets } from 'react-native-safe-area-context'
import type { BottomTabBarProps } from '@react-navigation/bottom-tabs'
import { House, MapPin, BookMarked, User } from 'lucide-react-native'

type TabConfig = {
  name: string
  label: string
  Icon: React.ComponentType<{ size: number; color: string }>
}

const TABS: TabConfig[] = [
  { name: 'index',             label: 'Início',  Icon: House      },
  { name: 'collection-points', label: 'Pontos',  Icon: MapPin     },
  { name: 'reservations',      label: 'Reservas', Icon: BookMarked },
  { name: 'profile',           label: 'Perfil',  Icon: User       },
]

export function AppNavbar({ state, navigation }: BottomTabBarProps) {
  const insets = useSafeAreaInsets()

  return (
    <View
      className="bg-white border-t border-gray-200 flex-row"
      style={{ paddingBottom: insets.bottom }}
    >
      {state.routes.map((route, index) => {
        const tab = TABS.find(t => t.name === route.name)
        if (!tab) return null

        const isFocused = state.index === index
        const color = isFocused ? '#059669' : '#9ca3af'

        function onPress() {
          const event = navigation.emit({
            type: 'tabPress',
            target: route.key,
            canPreventDefault: true,
          })
          if (!isFocused && !event.defaultPrevented) {
            navigation.navigate(route.name)
          }
        }

        return (
          <TouchableOpacity
            key={route.key}
            className="flex-1 items-center py-3 gap-y-1"
            activeOpacity={0.7}
            onPress={onPress}
          >
            <tab.Icon size={22} color={color} />
            <Text className={`text-xs ${isFocused ? 'text-emerald-600 font-medium' : 'text-gray-400'}`}>
              {tab.label}
            </Text>
          </TouchableOpacity>
        )
      })}
    </View>
  )
}
