import { useState } from 'react'
import {
  Modal,
  View,
  Text,
  ScrollView,
  KeyboardAvoidingView,
  Platform,
  TouchableOpacity,
  ActivityIndicator,
} from 'react-native'

import { X, CheckCircle2 } from 'lucide-react-native'
import { BOOK_CATEGORIES } from '../../../../types/book.types'
import { Input } from '../../../../components/Input'
import { Button } from '../../../../components/Button'
import { CategoryChip } from '../../-components/CategoryChip'
import { CollectionPointPicker } from './CollectionPointPicker'
import { useDonateBook } from '../../../../hooks/useCollectionPoints'

type Props = {
  visible: boolean
  onClose: () => void
}

type FormErrors = {
  collectionPointId?: string
  bookTitle?: string
  bookAuthor?: string
  bookCategory?: string
}

const EMPTY_FORM = {
  collectionPointId: null as number | null,
  bookTitle: '',
  bookAuthor: '',
  bookPublisher: '',
  bookCategory: null as number | null,
  bookIsbn: '',
}

export function DonateModal({ visible, onClose }: Props) {
  const [form, setForm] = useState(EMPTY_FORM)
  const [errors, setErrors] = useState<FormErrors>({})
  const [errorMessage, setErrorMessage] = useState<string | null>(null)
  const [success, setSuccess] = useState(false)

  const { mutate, isPending } = useDonateBook()

  function resetAndClose() {
    setForm(EMPTY_FORM)
    setErrors({})
    setErrorMessage(null)
    setSuccess(false)
    onClose()
  }

  function validate(): boolean {
    const next: FormErrors = {}
    if (form.collectionPointId === null) next.collectionPointId = 'Selecione um ponto de coleta'
    if (!form.bookTitle.trim()) next.bookTitle = 'O título é obrigatório'
    if (!form.bookAuthor.trim()) next.bookAuthor = 'O autor é obrigatório'
    if (form.bookCategory === null) next.bookCategory = 'Selecione uma categoria'
    setErrors(next)
    return Object.keys(next).length === 0
  }

  function handleSubmit() {
    if (!validate()) return
    setErrorMessage(null)
    mutate(
      {
        collectionPointId: form.collectionPointId!,
        bookTitle: form.bookTitle.trim(),
        bookAuthor: form.bookAuthor.trim(),
        bookPublisher: form.bookPublisher.trim() || undefined,
        bookCategory: form.bookCategory!,
        bookIsbn: form.bookIsbn.trim() || undefined,
      },
      {
        onSuccess: () => setSuccess(true),
        onError: (error: unknown) => {
          const msg =
            (error as { response?: { data?: { message?: string } } })?.response?.data?.message ??
            'Erro ao enviar a doação. Tente novamente.'
          setErrorMessage(msg)
        },
      },
    )
  }

  return (
    <Modal visible={visible} transparent animationType="slide" onRequestClose={resetAndClose}>
      <KeyboardAvoidingView
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        className="flex-1 justify-end"
      >
        <View className="flex-1 bg-black/50" />

        {success ? (
          <View className="bg-emerald-800 rounded-t-3xl px-6 pt-6 pb-10 items-center gap-5">
            <CheckCircle2 size={48} color="#a7f3d0" />
            <Text className="text-white font-bold text-xl text-center">Doação enviada!</Text>
            <Text className="text-emerald-200 text-sm text-center">
              Aguarde o administrador do ponto de coleta confirmar o recebimento.
            </Text>
            <Button label="Fechar" variant="secondary" onPress={resetAndClose} />
          </View>
        ) : (
          <View className="bg-emerald-800 rounded-t-3xl">
            <ScrollView
              className="px-6 pt-6"
              contentContainerStyle={{ paddingBottom: 40 }}
              keyboardShouldPersistTaps="handled"
              showsVerticalScrollIndicator={false}
            >
              <View className="flex-row items-center justify-between mb-6">
                <Text className="text-white font-bold text-xl">Doar um Livro</Text>
                <TouchableOpacity onPress={resetAndClose} hitSlop={8}>
                  <X size={22} color="#a7f3d0" />
                </TouchableOpacity>
              </View>

              <CollectionPointPicker
                value={form.collectionPointId}
                onChange={(id) => {
                  setForm((f) => ({ ...f, collectionPointId: id }))
                  setErrors((e) => ({ ...e, collectionPointId: undefined }))
                }}
                error={errors.collectionPointId}
              />

              <Input
                label="Título do livro *"
                value={form.bookTitle}
                onChangeText={(text) => {
                  setForm((f) => ({ ...f, bookTitle: text }))
                  setErrors((e) => ({ ...e, bookTitle: undefined }))
                }}
                error={errors.bookTitle}
                placeholder="Ex: Dom Casmurro"
                placeholderTextColor="#6ee7b7"
                inputClassName="bg-white/10 text-white"
              />

              <Input
                label="Autor *"
                value={form.bookAuthor}
                onChangeText={(text) => {
                  setForm((f) => ({ ...f, bookAuthor: text }))
                  setErrors((e) => ({ ...e, bookAuthor: undefined }))
                }}
                error={errors.bookAuthor}
                placeholder="Ex: Machado de Assis"
                placeholderTextColor="#6ee7b7"
                inputClassName="bg-white/10 text-white"
              />

              <Input
                label="Editora"
                value={form.bookPublisher}
                onChangeText={(text) => setForm((f) => ({ ...f, bookPublisher: text }))}
                placeholder="Opcional"
                placeholderTextColor="#6ee7b7"
                inputClassName="bg-white/10 text-white"
              />

              <View className="mb-4">
                <Text className="text-sm font-medium text-white mb-2">Categoria *</Text>
                <ScrollView horizontal showsHorizontalScrollIndicator={false}>
                  {BOOK_CATEGORIES.map((cat) => (
                    <CategoryChip
                      key={cat.code}
                      label={cat.label}
                      selected={form.bookCategory === cat.code}
                      onPress={() => {
                        setForm((f) => ({ ...f, bookCategory: cat.code }))
                        setErrors((e) => ({ ...e, bookCategory: undefined }))
                      }}
                    />
                  ))}
                </ScrollView>
                {errors.bookCategory ? (
                  <Text className="text-red-400 text-xs mt-1">{errors.bookCategory}</Text>
                ) : null}
              </View>

              <Input
                label="ISBN"
                value={form.bookIsbn}
                onChangeText={(text) => setForm((f) => ({ ...f, bookIsbn: text }))}
                placeholder="Opcional"
                placeholderTextColor="#6ee7b7"
                inputClassName="bg-white/10 text-white"
                keyboardType="numeric"
              />

              {errorMessage ? (
                <View className="bg-red-900/40 rounded-xl px-4 py-3 mb-4">
                  <Text className="text-red-300 text-sm text-center">{errorMessage}</Text>
                </View>
              ) : null}

              {isPending ? (
                <ActivityIndicator size="small" color="#a7f3d0" className="mt-2" />
              ) : (
                <Button label="Enviar Doação" variant="primary" onPress={handleSubmit} />
              )}
            </ScrollView>
          </View>
        )}
      </KeyboardAvoidingView>
    </Modal>
  )
}
