import { BookCategoryEnum, BookInstanceStatus } from '../constants/enums'

export type BookCategory = {
  code: number
  key: string
  label: string
}

export const BOOK_CATEGORIES: BookCategory[] = [
  { code: 1, key: 'INFANTIL_JUVENIL', label: 'Infantil/Juvenil' },
  { code: 2, key: 'AUTOAJUDA',        label: 'Autoajuda' },
  { code: 3, key: 'DIDATICO',         label: 'Didático' },
  { code: 4, key: 'ESCOLAR',          label: 'Escolar' },
]

export type BookInstance = {
  id: number
  bookId: number
  bookTitle: string
  bookAuthor: string
  bookIsbn: string
  bookCategory: BookCategoryEnum
  status: BookInstanceStatus
  collectionPointId: number
  collectionPointName: string
  userDonorId: number | null
  userDonorName: string | null
  bookThumbnailUrl: string | null
  createdAt: string
  updatedAt: string
}

export const MOCK_BOOK_INSTANCES: BookInstance[] = [
  {
    id: 1,
    bookId: 1,
    bookTitle: 'O Cortiço',
    bookAuthor: 'Aluísio Azevedo',
    bookIsbn: '978-8535902776',
    bookCategory: 'DIDATICO',
    status: 'DISPONIVEL',
    collectionPointId: 1,
    collectionPointName: 'Ponto Duque de Caxias Centro',
    userDonorId: null,
    userDonorName: null,
    imageUrl: null,
    createdAt: '2025-01-10T10:00:00',
    updatedAt: '2025-01-10T10:00:00',
  },
  {
    id: 2,
    bookId: 2,
    bookTitle: 'Dom Casmurro',
    bookAuthor: 'Machado de Assis',
    bookIsbn: '978-8535910663',
    bookCategory: 'ESCOLAR',
    status: 'RESERVADO',
    collectionPointId: 2,
    collectionPointName: 'Ponto Nova Iguaçu',
    userDonorId: null,
    userDonorName: null,
    imageUrl: null,
    createdAt: '2025-01-12T14:00:00',
    updatedAt: '2025-01-12T14:00:00',
  },
]
