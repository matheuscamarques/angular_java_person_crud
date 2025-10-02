export enum PersonStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  PENDING_VERIFICATION = 'PENDING_VERIFICATION',
}

// Interface completa da Pessoa
export interface Person {
  id: string; // UUID é uma string
  name: string;
  email: string;
  phone?: string; // Opcional, pois pode ser nulo
  birthDate: string; // LocalDate será uma string no formato 'YYYY-MM-DD'
  status: PersonStatus;
  balance: number; // BigDecimal é mapeado para number
  currency: string;
  createdAt: string; // LocalDateTime será uma string ISO
  updatedAt: string; // LocalDateTime será uma string ISO
}
