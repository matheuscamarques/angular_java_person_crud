export interface Page<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number; // número da página atual (base 0)
  size: number;
}