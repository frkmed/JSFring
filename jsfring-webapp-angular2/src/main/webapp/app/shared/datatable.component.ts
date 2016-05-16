import { Component, ContentChild, ContentChildren, Input, Output, EventEmitter, OnInit, QueryList } from '@angular/core';

import { Button, Checkbox, Column, DataTable, Dialog } from 'primeng/primeng';

import { CrudService } from './crud.service';

@Component({
    selector: 'p62-datatable-columns',
    template: '',
    directives: [Column]
})
export class DatatableColumnsComponent {

    @ContentChildren(Column) columns: QueryList<Column>;

}

/**
 * Wrapper for {@link primeng/primeng/DataTable}.
 * @param service The CRUD service.
 * @param selectedValue The selected value.
 * @param <p62-datatable-columns> The {@link primeng/primeng/Column}s definition.
 * @param <p62-datatable-dialog> The {@link primeng/primeng/Dialog}s content.
 */
@Component({
    selector: 'p62-datatable',
    templateUrl: './app/shared/datatable.component.html',
    directives: [Button, Column, DataTable, DatatableColumnsComponent, Dialog]
})
export class DatatableComponent {

    @ContentChild(DatatableColumnsComponent) datatableColumns: DatatableColumnsComponent;

    /** {@link CrudService} used to CRUD operations: list, create, update and delete items. */
    @Input() service: CrudService<any>;

    values: Array<any>;

    /** The current value: creation, modification or deletion. */
    @Input() selectedValue: any;
    @Output() selectedValueChange: EventEmitter<any> = new EventEmitter();

    displayDialog: boolean;

    /** Distinguishes the creation and modification, because two operations use the same dialog. */
    isCreation: boolean;

    ngOnInit() {
        this.refresh();
    }

    refresh() {
        this.service.findAll().subscribe(res => this.values = res);
    }

    onRowSelect(event) {
        this.selectedValueChange.emit(event.data);
        this.selectedValue = this.clone(event.data);
        this.displayDialog = true;
    }

    /**
     * Clone the object.
     * @param src The source object to clone.
     * @return The new cloned object.
     */
    private clone(src: any): any {
        let tgt = new (<any>src.constructor)();
        for (let prop in src)
            tgt[prop] = src[prop];
        return tgt;
    }

    /**
     * Click on "Save" button of Dialog.<br>
     * Call the create() or update() method, depending on whether it is a creation or a modification.
     */
    save() {
        this.isCreation ? this.create() : this.update();
    }

    private create() {
        this.service.create(this.selectedValue);
        this.displayDialog = false;
        this.refresh();
    }

    private update() {
        this.service.update(this.selectedValue);
        this.displayDialog = false;
        this.refresh();
    }

    /**
     * Click on "Delete" button of Dialog.
     */
    delete() {
        //        this.service.delete(this.selectedValue);
        this.displayDialog = false;
        this.refresh();
    }

}