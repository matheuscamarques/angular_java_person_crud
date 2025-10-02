import { CommonModule, CurrencyPipe, DatePipe, TitleCasePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Person, PersonStatus } from '../models/person.model';
import { catchError, EMPTY } from 'rxjs';

@Component({
  selector: 'app-person-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    DatePipe,
    CurrencyPipe,
    TitleCasePipe
  ],
  templateUrl: './person-detail.component.html',
  styleUrl: './person-detail.component.scss'
})
export class PersonDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);
  person = signal<Person | null>(null);
  isLoading = signal(true);
  error = signal<string | null>(null);

  private readonly API_URL = 'http://localhost:8080/persons';

  ngOnInit(): void {
    // 1. Pega o 'id' dos parâmetros da rota atual
    const personId = this.route.snapshot.paramMap.get('id');

    if (!personId) {
      this.error.set('ID da pessoa não fornecido na URL.');
      this.isLoading.set(false);
      return;
    }

    // 2. Busca os dados da pessoa na API
    this.fetchPersonDetails(personId);
  }

  fetchPersonDetails(id: string): void {
    this.isLoading.set(true);
    this.http.get<Person>(`${this.API_URL}/${id}`).pipe(
      catchError(() => {
        // 3. Em caso de erro (ex: 404 Not Found), atualiza o estado de erro
        this.error.set('Pessoa não encontrada ou erro ao carregar os dados.');
        this.isLoading.set(false);
        return EMPTY; // Encerra o fluxo do observable para evitar erros no subscribe
      })
    ).subscribe(data => {
      // 4. Com sucesso, atualiza o signal com os dados da pessoa
      this.person.set(data);
      this.isLoading.set(false);
    });
  }

  getStatusClass(status: PersonStatus): string {
    switch (status) {
      case PersonStatus.ACTIVE: return 'bg-green-100 text-green-800';
      case PersonStatus.INACTIVE: return 'bg-slate-100 text-slate-800';
      case PersonStatus.PENDING_VERIFICATION: return 'bg-yellow-100 text-yellow-800';
      default: return 'bg-gray-100';
    }
  }
}
