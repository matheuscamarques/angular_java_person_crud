import { Component, inject, signal } from '@angular/core';
import { Person, PersonStatus } from '../models/person.model';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule, Location } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { catchError, EMPTY, Observable } from 'rxjs';

interface PersonCreateDTO {
  name: string;
  email: string;
  phone?: string;
  birthDate: string;
  status: PersonStatus;
  balance: number;
  currency: string;
}

@Component({
  selector: 'app-person-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './person-form.component.html',
  styleUrl: './person-form.component.scss'
})
export class PersonFormComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private location = inject(Location);

  private readonly API_URL = 'http://localhost:8080/persons';

  isEditing = signal(false);
  isLoading = signal(false);
  error = signal<string | null>(null);
  pageTitle = signal('Nova Pessoa');
  private personId = signal<string | null>(null);

  personForm: FormGroup;
  personStatusOptions = Object.values(PersonStatus);

  constructor() {
    this.personForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      phone: [''],
      birthDate: ['', Validators.required],
      status: [PersonStatus.ACTIVE, Validators.required],
      balance: [0, [Validators.required, Validators.min(0)]],
      currency: ['BRL', Validators.required]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditing.set(true);
      this.personId.set(id);
      this.pageTitle.set('Editar Pessoa');
      this.loadPersonData(id);
    }
  }

  loadPersonData(id: string): void {
    this.isLoading.set(true);
    this.http.get<Person>(`${this.API_URL}/${id}`).pipe(
      catchError(() => {
        this.error.set('Não foi possível carregar os dados da pessoa.');
        this.isLoading.set(false);
        return EMPTY;
      })
    ).subscribe(person => {
      // Usamos patchValue para preencher o formulário com os dados recebidos
      this.personForm.patchValue(person);
      this.isLoading.set(false);
    });
  }

  savePerson(): void {
    // Marca todos os campos como "tocados" para exibir os erros de validação
    if (this.personForm.invalid) {
      this.personForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    this.error.set(null);

    const personData: PersonCreateDTO = this.personForm.value;
    let saveObservable: Observable<Person>;

    if (this.isEditing()) {
      // Se está editando, faz uma requisição PUT
      saveObservable = this.http.put<Person>(`${this.API_URL}/${this.personId()}`, personData);
    } else {
      // Se está criando, faz uma requisição POST
      saveObservable = this.http.post<Person>(this.API_URL, personData);
    }

    saveObservable.pipe(
      catchError(err => {
        const errorMessage = err.error?.message || 'Ocorreu um erro ao salvar. Verifique os dados e tente novamente.';
        this.error.set(errorMessage);
        this.isLoading.set(false);
        return EMPTY;
      })
    ).subscribe(() => {
      this.isLoading.set(false);
      // Navega de volta para a lista de pessoas após o sucesso
      this.router.navigate(['/persons']);
    });
  }

   goBack(): void {
    this.location.back();
  }
}
