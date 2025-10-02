import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Person, PersonStatus } from '../models/person.model';
import { Page } from '../interfaces/page.interface';
import { catchError, EMPTY } from 'rxjs';

@Component({
  selector: 'app-person-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    ReactiveFormsModule,
    DatePipe,
    CurrencyPipe
  ],
  templateUrl: './person-list.component.html',
  styleUrl: './person-list.component.scss'
})
export class PersonListComponent {
  private http = inject(HttpClient);
  private fb = inject(FormBuilder);

  private readonly API_URL = 'http://localhost:8080/persons';

  persons = signal<Person[]>([]);
  isLoading = signal(true);
  error = signal<string | null>(null);
  personToDelete = signal<Person | null>(null);

  currentPage = signal(0);
  totalPages = signal(0);
  totalElements = signal(0);
  pageSize = signal(10);

  filterForm: FormGroup;
  personStatusOptions = Object.values(PersonStatus); 

   constructor() {
    this.filterForm = this.fb.group({
      name: [''],
      email: ['']
    });
  }

  ngOnInit(): void {
    this.fetchPersons();
  }

  fetchPersons(): void {
    this.isLoading.set(true);
    this.error.set(null);

    let params = new HttpParams()
      .set('page', this.currentPage().toString())
      .set('size', this.pageSize().toString());

    // Adiciona filtros se existirem
    const filters = this.filterForm.value;
    if (filters.name) {
      params = params.append('name', filters.name);
    }
    if (filters.email) {
      params = params.append('email', filters.email);
    }

    this.http.get<Page<Person>>(this.API_URL, { params }).pipe(
      catchError(() => {
        this.error.set('Falha ao carregar a lista de pessoas. Tente novamente mais tarde.');
        this.isLoading.set(false);
        this.persons.set([]);
        return EMPTY;
      })
    ).subscribe(response => {
      this.persons.set(response.content);
      this.totalPages.set(response.totalPages);
      this.totalElements.set(response.totalElements);
      this.currentPage.set(response.number);
      this.isLoading.set(false);
    });
  }

  applyFilters(): void {
    this.currentPage.set(0); // Volta para a primeira página ao aplicar filtros
    this.fetchPersons();
  }

  resetFilters(): void {
    this.filterForm.reset({ name: '', email: '' });
    this.applyFilters();
  }

  changePage(newPage: number): void {
    if (newPage >= 0 && newPage < this.totalPages()) {
      this.currentPage.set(newPage);
      this.fetchPersons();
    }
  }

  confirmDelete(): void {
    const person = this.personToDelete();
    if (!person) return;

    this.http.delete(`${this.API_URL}/${person.id}`).pipe(
      catchError(() => {
        this.error.set(`Erro ao excluir ${person.name}.`);
        return EMPTY;
      })
    ).subscribe(() => {
      this.personToDelete.set(null);
      this.fetchPersons(); // Recarrega a lista após a exclusão
    });
  }

  cancelDelete(): void {
    this.personToDelete.set(null);
  }

  deletePerson(person: Person): void {
    this.personToDelete.set(person);
  }

    getStatusClass(status: PersonStatus): string {
    const baseClasses = 'px-3 py-1 text-xs font-semibold rounded-full inline-block';
    switch (status) {
      case PersonStatus.ACTIVE: return `${baseClasses} bg-green-100 text-green-800`;
      case PersonStatus.INACTIVE: return `${baseClasses} bg-gray-200 text-gray-800`;
      case PersonStatus.PENDING_VERIFICATION: return `${baseClasses} bg-yellow-100 text-yellow-800`;
      default: return `${baseClasses} bg-gray-100`;
    }
  }
} 
