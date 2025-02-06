import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filter',
})
export class FilterPipe implements PipeTransform {
  transform(items: any[], searchQuery: string, keys: string[]): any[] {
    if (!items || !searchQuery) {
      return items;
    }

    searchQuery = searchQuery.toLowerCase();

    return items.filter((item) =>
      keys.some((key) =>
        item[key]?.toString().toLowerCase().includes(searchQuery)
      )
    );
  }
}
