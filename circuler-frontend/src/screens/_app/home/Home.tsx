import { useState } from 'react'
import { ScrollView, View, ActivityIndicator } from 'react-native'
import { SafeAreaView } from 'react-native-safe-area-context'
import { HeroCard } from './-components/HeroCard'
import { CategoryFilter } from './-components/CategoryFilter'
import { BookInstanceList } from './-components/BookInstanceList'
import { useBookInstances } from '../../../hooks/useBookInstances'

export default function Home() {
  const [selectedCategory, setSelectedCategory] = useState<number | null>(null)
  const { data: instances = [], isLoading } = useBookInstances()

  return (
    <SafeAreaView className="flex-1 bg-white">
      <ScrollView contentContainerStyle={{ paddingBottom: 24 }}>
        <View className="px-4">
          <HeroCard />
        </View>
        <View className="mt-6 px-4">
          <CategoryFilter
            selected={selectedCategory}
            onSelect={setSelectedCategory}
          />
        </View>
        <View className="mt-6 px-4">
          {isLoading ? (
            <ActivityIndicator size="large" color="#059669" />
          ) : (
            <BookInstanceList instances={instances} />
          )}
        </View>
      </ScrollView>
    </SafeAreaView>
  )
}
