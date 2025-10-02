import { Routes } from '@angular/router';
import { PersonFormComponent } from './person-form/person-form.component';
import { PersonListComponent } from './person-list/person-list.component';
import { PersonDetailComponent } from './person-detail/person-detail.component';

export const routes: Routes = [
    // Rota para a lista de pessoas
    { path: 'persons', component: PersonListComponent },

    // Rota para criar uma nova pessoa
    { path: 'person/new', component: PersonFormComponent },

    // Rota para editar uma pessoa existente (o :id é um parâmetro dinâmico)
    { path: 'person/:id', component: PersonFormComponent },

    { path: 'person/details/:id', component: PersonDetailComponent },

    // Rota padrão: redireciona para a lista de pessoas se a URL estiver vazia
    { path: '', redirectTo: '/persons', pathMatch: 'full' },

    // Rota "catch-all": redireciona para a lista se a URL não for encontrada
    { path: '**', redirectTo: '/persons' }
];
