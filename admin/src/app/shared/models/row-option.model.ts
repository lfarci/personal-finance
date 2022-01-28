export interface RowOption<Resource> {
  label: string;
  icon?: string;
  action: (resource: Resource) => void;
}
