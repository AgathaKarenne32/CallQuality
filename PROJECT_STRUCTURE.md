# CallQuality AI - Portal de Documentação
## Estrutura Completa do Projeto

---

## 📁 /App.tsx

```tsx
import { useState } from 'react';
import { Sidebar } from './components/Sidebar';
import { OverviewSection } from './components/OverviewSection';
import { FunctionalRequirements } from './components/FunctionalRequirements';
import { NonFunctionalRequirements } from './components/NonFunctionalRequirements';
import { BusinessRules } from './components/BusinessRules';
import { DatabaseArchitecture } from './components/DatabaseArchitecture';

export type Section = 'overview' | 'functional' | 'non-functional' | 'business-rules' | 'database';

export default function App() {
  const [activeSection, setActiveSection] = useState<Section>('overview');

  const renderSection = () => {
    switch (activeSection) {
      case 'overview':
        return <OverviewSection />;
      case 'functional':
        return <FunctionalRequirements />;
      case 'non-functional':
        return <NonFunctionalRequirements />;
      case 'business-rules':
        return <BusinessRules />;
      case 'database':
        return <DatabaseArchitecture />;
      default:
        return <OverviewSection />;
    }
  };

  return (
    <div className="flex min-h-screen bg-[#F1F5F9]">
      <Sidebar activeSection={activeSection} onSectionChange={setActiveSection} />
      <main className="flex-1 ml-[260px]">
        {renderSection()}
      </main>
    </div>
  );
}
```

---

## 📁 /components/Sidebar.tsx

```tsx
import { Home, FileText, Shield, Scale, Database } from 'lucide-react';
import { Section } from '../App';

interface SidebarProps {
  activeSection: Section;
  onSectionChange: (section: Section) => void;
}

export function Sidebar({ activeSection, onSectionChange }: SidebarProps) {
  const menuItems = [
    { id: 'overview' as Section, label: 'Visão Geral', icon: Home },
    { id: 'functional' as Section, label: 'Requisitos Funcionais', icon: FileText },
    { id: 'non-functional' as Section, label: 'Requisitos Não Funcionais', icon: Shield },
    { id: 'business-rules' as Section, label: 'Regras de Negócio', icon: Scale },
    { id: 'database' as Section, label: 'Banco de Dados', icon: Database },
  ];

  return (
    <aside className="fixed left-0 top-0 h-screen w-[260px] bg-white border-r border-[#E2E8F0] overflow-y-auto">
      <div className="p-6 border-b border-[#E2E8F0]">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-[#4F46E5] rounded-lg flex items-center justify-center text-white">
            C
          </div>
          <span className="text-[#0F172A]">CallQuality</span>
        </div>
      </div>

      <nav className="p-4">
        <div className="space-y-1">
          {menuItems.map((item) => {
            const Icon = item.icon;
            const isActive = activeSection === item.id;

            return (
              <button
                key={item.id}
                onClick={() => onSectionChange(item.id)}
                className={`w-full flex items-center gap-3 px-4 py-3 rounded-lg transition-all ${
                  isActive
                    ? 'bg-[#EEF2FF] text-[#4F46E5]'
                    : 'text-[#64748B] hover:bg-[#F8FAFC]'
                }`}
              >
                <Icon className="w-5 h-5" />
                <span>{item.label}</span>
              </button>
            );
          })}
        </div>
      </nav>

      <div className="p-4 mt-auto">
        <div className="bg-[#F8FAFC] rounded-lg p-4 text-[#64748B]">
          <div className="flex items-center gap-2 mb-2">
            <div className="w-2 h-2 bg-[#10B981] rounded-full"></div>
            <span>Versão 1.0.0</span>
          </div>
          <p className="text-xs">
            Portal de Documentação CallQuality AI
          </p>
        </div>
      </div>
    </aside>
  );
}
```

---

## 📁 /components/OverviewSection.tsx

```tsx
import { Code2, Database, Package } from 'lucide-react';

export function OverviewSection() {
  return (
    <div className="p-8">
      <div className="mb-8">
        <div className="flex items-center gap-3 mb-2">
          <h1 className="text-[#0F172A]">Visão Geral do Projeto</h1>
          <span className="px-3 py-1 bg-[#4F46E5] text-white rounded-full text-xs">
            Versão 1.0.0
          </span>
        </div>
        <p className="text-[#64748B]">
          Documentação técnica completa do CallQuality AI
        </p>
      </div>

      <div className="grid grid-cols-3 gap-6 mb-8">
        <div className="bg-white rounded-lg p-6 shadow-sm">
          <div className="flex items-center justify-between mb-4">
            <div className="w-12 h-12 bg-[#EEF2FF] rounded-lg flex items-center justify-center">
              <Package className="w-6 h-6 text-[#4F46E5]" />
            </div>
          </div>
          <div className="text-[#64748B] mb-1">Total de Requisitos</div>
          <div className="text-[#0F172A]">15</div>
          <div className="text-xs text-[#64748B] mt-2">8 RF + 7 RNF</div>
        </div>

        <div className="bg-white rounded-lg p-6 shadow-sm">
          <div className="flex items-center justify-between mb-4">
            <div className="w-12 h-12 bg-[#ECFDF5] rounded-lg flex items-center justify-center">
              <Database className="w-6 h-6 text-[#10B981]" />
            </div>
          </div>
          <div className="text-[#64748B] mb-1">Tabelas no Banco</div>
          <div className="text-[#0F172A]">6</div>
          <div className="text-xs text-[#64748B] mt-2">Arquitetura relacional</div>
        </div>

        <div className="bg-white rounded-lg p-6 shadow-sm">
          <div className="flex items-center justify-between mb-4">
            <div className="w-12 h-12 bg-[#FEF3C7] rounded-lg flex items-center justify-center">
              <Code2 className="w-6 h-6 text-[#F59E0B]" />
            </div>
          </div>
          <div className="text-[#64748B] mb-1">Stack Tecnológica</div>
          <div className="text-[#0F172A]">Java + Spring</div>
          <div className="text-xs text-[#64748B] mt-2">MySQL 8.0</div>
        </div>
      </div>

      <div className="bg-white rounded-lg p-8 shadow-sm mb-6">
        <h2 className="text-[#0F172A] mb-4">Sobre o Projeto</h2>
        <div className="space-y-4 text-[#0F172A]">
          <p>
            O <strong>CallQuality AI</strong> é uma solução inovadora para análise automatizada de qualidade 
            em ligações de atendimento ao cliente. O sistema utiliza inteligência artificial para avaliar 
            critérios como clareza, objetividade e cortesia, gerando relatórios detalhados e fornecendo 
            feedback em tempo real aos atendentes.
          </p>
          
          <div className="bg-[#F8FAFC] rounded-lg p-6">
            <h3 className="text-[#0F172A] mb-3">Objetivos Principais</h3>
            <ul className="space-y-2 text-[#0F172A]">
              <li className="flex items-start gap-2">
                <span className="text-[#4F46E5] mt-1">•</span>
                <span>Automatizar a avaliação de qualidade das ligações</span>
              </li>
              <li className="flex items-start gap-2">
                <span className="text-[#4F46E5] mt-1">•</span>
                <span>Fornecer feedback em tempo real aos atendentes</span>
              </li>
              <li className="flex items-start gap-2">
                <span className="text-[#4F46E5] mt-1">•</span>
                <span>Gerar relatórios gerenciais para tomada de decisão</span>
              </li>
              <li className="flex items-start gap-2">
                <span className="text-[#4F46E5] mt-1">•</span>
                <span>Identificar padrões e oportunidades de melhoria</span>
              </li>
            </ul>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-6">
        <div className="bg-white rounded-lg p-6 shadow-sm">
          <h3 className="text-[#0F172A] mb-3">Tecnologias Backend</h3>
          <div className="space-y-2">
            <div className="flex items-center gap-3 px-4 py-2 bg-[#F8FAFC] rounded-lg">
              <div className="w-8 h-8 bg-[#b07219] rounded flex items-center justify-center text-white text-xs">
                J
              </div>
              <div>
                <div className="text-[#0F172A]">Java 17</div>
                <div className="text-xs text-[#64748B]">LTS Version</div>
              </div>
            </div>
            <div className="flex items-center gap-3 px-4 py-2 bg-[#F8FAFC] rounded-lg">
              <div className="w-8 h-8 bg-[#6DB33F] rounded flex items-center justify-center text-white text-xs">
                S
              </div>
              <div>
                <div className="text-[#0F172A]">Spring Boot</div>
                <div className="text-xs text-[#64748B]">Framework principal</div>
              </div>
            </div>
            <div className="flex items-center gap-3 px-4 py-2 bg-[#F8FAFC] rounded-lg">
              <div className="w-8 h-8 bg-[#00758F] rounded flex items-center justify-center text-white text-xs">
                M
              </div>
              <div>
                <div className="text-[#0F172A]">MySQL 8.0</div>
                <div className="text-xs text-[#64748B]">Banco de dados</div>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg p-6 shadow-sm">
          <h3 className="text-[#0F172A] mb-3">Funcionalidades Core</h3>
          <div className="space-y-2">
            <div className="px-4 py-3 bg-[#F8FAFC] rounded-lg">
              <div className="text-[#0F172A] mb-1">Autenticação JWT</div>
              <div className="text-xs text-[#64748B]">Sistema seguro de login</div>
            </div>
            <div className="px-4 py-3 bg-[#F8FAFC] rounded-lg">
              <div className="text-[#0F172A] mb-1">Análise por IA</div>
              <div className="text-xs text-[#64748B]">Avaliação automatizada</div>
            </div>
            <div className="px-4 py-3 bg-[#F8FAFC] rounded-lg">
              <div className="text-[#0F172A] mb-1">Relatórios</div>
              <div className="text-xs text-[#64748B]">Dashboards gerenciais</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
```

---

## 📁 /components/FunctionalRequirements.tsx

```tsx
import { useState } from 'react';
import { Filter } from 'lucide-react';

interface Requirement {
  id: string;
  name: string;
  description: string;
  actor: string;
  priority: 'Alta' | 'Média';
}

export function FunctionalRequirements() {
  const [filter, setFilter] = useState<string>('Todos');

  const requirements: Requirement[] = [
    {
      id: 'RF001',
      name: 'Autenticação de Usuário',
      description: 'O sistema deve permitir que usuários façam login usando email e senha, com autenticação via JWT.',
      actor: 'Atendente/Supervisor',
      priority: 'Alta'
    },
    {
      id: 'RF002',
      name: 'Upload de Áudio',
      description: 'O sistema deve permitir o upload de arquivos de áudio no formato MP3 ou WAV para análise.',
      actor: 'Atendente',
      priority: 'Alta'
    },
    {
      id: 'RF003',
      name: 'Análise Automatizada',
      description: 'O sistema deve processar o áudio e gerar uma avaliação baseada em critérios de qualidade (clareza, cortesia, resolução).',
      actor: 'Sistema',
      priority: 'Alta'
    },
    {
      id: 'RF004',
      name: 'Visualização de Resultados',
      description: 'O atendente deve poder visualizar a pontuação e o feedback detalhado da sua ligação.',
      actor: 'Atendente',
      priority: 'Alta'
    },
    {
      id: 'RF005',
      name: 'Dashboard Gerencial',
      description: 'Supervisores devem ter acesso a um painel com métricas agregadas (média de pontuações, número de ligações analisadas).',
      actor: 'Supervisor',
      priority: 'Alta'
    },
    {
      id: 'RF006',
      name: 'Filtragem de Ligações',
      description: 'O supervisor deve poder filtrar ligações por período, atendente ou pontuação.',
      actor: 'Supervisor',
      priority: 'Média'
    },
    {
      id: 'RF007',
      name: 'Exportação de Relatórios',
      description: 'O sistema deve permitir a exportação de relatórios em formato PDF ou Excel.',
      actor: 'Supervisor',
      priority: 'Média'
    },
    {
      id: 'RF008',
      name: 'Gestão de Usuários',
      description: 'Administradores devem poder criar, editar e desativar usuários no sistema.',
      actor: 'Administrador',
      priority: 'Alta'
    }
  ];

  const actors = ['Todos', 'Atendente', 'Supervisor', 'Sistema', 'Administrador'];
  
  const filteredRequirements = filter === 'Todos' 
    ? requirements 
    : requirements.filter(req => req.actor.includes(filter));

  const getActorColor = (actor: string) => {
    if (actor.includes('Supervisor')) return 'bg-[#4F46E5] text-white';
    if (actor.includes('Atendente')) return 'bg-[#10B981] text-white';
    if (actor.includes('Sistema')) return 'bg-[#F59E0B] text-white';
    if (actor.includes('Administrador')) return 'bg-[#EF4444] text-white';
    return 'bg-[#64748B] text-white';
  };

  const getPriorityColor = (priority: string) => {
    return priority === 'Alta' ? 'text-[#EF4444]' : 'text-[#F59E0B]';
  };

  return (
    <div className="p-8">
      <div className="mb-8">
        <h1 className="text-[#0F172A] mb-2">Requisitos Funcionais</h1>
        <p className="text-[#64748B]">
          Funcionalidades que o sistema deve implementar
        </p>
      </div>

      <div className="bg-white rounded-lg shadow-sm mb-6 p-6">
        <div className="flex items-center justify-between mb-6">
          <div className="flex items-center gap-3">
            <Filter className="w-5 h-5 text-[#64748B]" />
            <span className="text-[#0F172A]">Filtrar por:</span>
          </div>
          <select 
            value={filter}
            onChange={(e) => setFilter(e.target.value)}
            className="px-4 py-2 border border-[#E2E8F0] rounded-lg text-[#0F172A] focus:outline-none focus:border-[#4F46E5]"
          >
            {actors.map(actor => (
              <option key={actor} value={actor}>{actor}</option>
            ))}
          </select>
        </div>

        <div className="space-y-4">
          {/* Table Header */}
          <div className="grid grid-cols-12 gap-4 px-4 py-3 bg-[#F8FAFC] rounded-lg">
            <div className="col-span-1 text-[#64748B]">ID</div>
            <div className="col-span-3 text-[#64748B]">Nome</div>
            <div className="col-span-5 text-[#64748B]">Descrição</div>
            <div className="col-span-2 text-[#64748B]">Ator</div>
            <div className="col-span-1 text-[#64748B]">Prioridade</div>
          </div>

          {/* Table Rows */}
          {filteredRequirements.map((req) => (
            <div 
              key={req.id}
              className="grid grid-cols-12 gap-4 px-4 py-4 border border-[#E2E8F0] rounded-lg hover:border-[#4F46E5] transition-colors"
            >
              <div className="col-span-1 text-[#4F46E5]">{req.id}</div>
              <div className="col-span-3 text-[#0F172A]">{req.name}</div>
              <div className="col-span-5 text-[#64748B]">{req.description}</div>
              <div className="col-span-2">
                <span className={`px-3 py-1 rounded-full text-xs ${getActorColor(req.actor)}`}>
                  {req.actor}
                </span>
              </div>
              <div className="col-span-1">
                <span className={`${getPriorityColor(req.priority)}`}>
                  {req.priority}
                </span>
              </div>
            </div>
          ))}
        </div>

        {filteredRequirements.length === 0 && (
          <div className="text-center py-12 text-[#64748B]">
            Nenhum requisito encontrado com este filtro
          </div>
        )}
      </div>

      <div className="bg-[#EEF2FF] border border-[#4F46E5] rounded-lg p-6">
        <h3 className="text-[#4F46E5] mb-2">Total de Requisitos Funcionais</h3>
        <p className="text-[#0F172A]">
          {requirements.length} requisitos funcionais documentados, cobrindo autenticação, 
          processamento de áudio, análise por IA e gestão de relatórios.
        </p>
      </div>
    </div>
  );
}
```

---

## 📁 /components/NonFunctionalRequirements.tsx

```tsx
import { Shield, Zap, Lock, Globe, BarChart3, Users, Clock } from 'lucide-react';

interface NFR {
  id: string;
  name: string;
  description: string;
  category: string;
  icon: any;
  metrics: string;
}

export function NonFunctionalRequirements() {
  const requirements: NFR[] = [
    {
      id: 'RNF001',
      name: 'Performance',
      description: 'A análise de uma ligação de até 10 minutos deve ser concluída em no máximo 30 segundos.',
      category: 'Desempenho',
      icon: Zap,
      metrics: 'Tempo de resposta < 30s'
    },
    {
      id: 'RNF002',
      name: 'Segurança de Dados',
      description: 'Todas as senhas devem ser armazenadas com hash BCrypt. Comunicação via HTTPS obrigatória.',
      category: 'Segurança',
      icon: Lock,
      metrics: 'BCrypt + HTTPS'
    },
    {
      id: 'RNF003',
      name: 'Escalabilidade',
      description: 'O sistema deve suportar até 10.000 usuários simultâneos sem degradação de performance.',
      category: 'Escalabilidade',
      icon: Users,
      metrics: '10k usuários simultâneos'
    },
    {
      id: 'RNF004',
      name: 'Disponibilidade',
      description: 'O sistema deve ter 99.5% de disponibilidade mensal (downtime máximo de 3.6 horas/mês).',
      category: 'Confiabilidade',
      icon: Clock,
      metrics: '99.5% uptime'
    },
    {
      id: 'RNF005',
      name: 'Compatibilidade',
      description: 'A aplicação web deve funcionar nos navegadores Chrome, Firefox, Safari e Edge (últimas 2 versões).',
      category: 'Compatibilidade',
      icon: Globe,
      metrics: 'Cross-browser'
    },
    {
      id: 'RNF006',
      name: 'Auditoria',
      description: 'Todas as ações críticas (login, upload, exclusão) devem ser registradas em logs.',
      category: 'Auditoria',
      icon: BarChart3,
      metrics: 'Logs completos'
    },
    {
      id: 'RNF007',
      name: 'Conformidade LGPD',
      description: 'O sistema deve estar em conformidade com a Lei Geral de Proteção de Dados (LGPD).',
      category: 'Conformidade',
      icon: Shield,
      metrics: 'LGPD compliant'
    }
  ];

  const getCategoryColor = (category: string) => {
    const colors: Record<string, string> = {
      'Desempenho': 'bg-[#10B981]',
      'Segurança': 'bg-[#EF4444]',
      'Escalabilidade': 'bg-[#4F46E5]',
      'Confiabilidade': 'bg-[#F59E0B]',
      'Compatibilidade': 'bg-[#06B6D4]',
      'Auditoria': 'bg-[#8B5CF6]',
      'Conformidade': 'bg-[#EC4899]'
    };
    return colors[category] || 'bg-[#64748B]';
  };

  return (
    <div className="p-8">
      <div className="mb-8">
        <h1 className="text-[#0F172A] mb-2">Requisitos Não Funcionais</h1>
        <p className="text-[#64748B]">
          Atributos de qualidade e restrições técnicas do sistema
        </p>
      </div>

      <div className="grid grid-cols-1 gap-6 mb-6">
        {requirements.map((req) => {
          const Icon = req.icon;
          return (
            <div 
              key={req.id}
              className="bg-white rounded-lg shadow-sm p-6 border border-[#E2E8F0] hover:border-[#4F46E5] transition-colors"
            >
              <div className="flex items-start gap-4">
                <div className={`w-12 h-12 ${getCategoryColor(req.category)} rounded-lg flex items-center justify-center flex-shrink-0`}>
                  <Icon className="w-6 h-6 text-white" />
                </div>
                
                <div className="flex-1">
                  <div className="flex items-center gap-3 mb-2">
                    <span className="text-[#4F46E5]">{req.id}</span>
                    <h3 className="text-[#0F172A]">{req.name}</h3>
                    <span className={`px-3 py-1 ${getCategoryColor(req.category)} text-white rounded-full text-xs ml-auto`}>
                      {req.category}
                    </span>
                  </div>
                  
                  <p className="text-[#0F172A] mb-3">
                    {req.description}
                  </p>
                  
                  <div className="flex items-center gap-2">
                    <div className="px-3 py-1 bg-[#F8FAFC] rounded text-xs text-[#64748B]">
                      📊 Métrica: {req.metrics}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          );
        })}
      </div>

      <div className="grid grid-cols-3 gap-6">
        <div className="bg-white rounded-lg shadow-sm p-6">
          <h3 className="text-[#0F172A] mb-4">Categorias</h3>
          <div className="space-y-2">
            {Array.from(new Set(requirements.map(r => r.category))).map(cat => (
              <div key={cat} className="flex items-center gap-2">
                <div className={`w-3 h-3 ${getCategoryColor(cat)} rounded-full`}></div>
                <span className="text-[#0F172A]">{cat}</span>
              </div>
            ))}
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-sm p-6">
          <h3 className="text-[#0F172A] mb-4">Prioridades</h3>
          <div className="space-y-3">
            <div>
              <div className="text-[#64748B] mb-1">Críticos</div>
              <div className="text-[#0F172A]">5 requisitos</div>
            </div>
            <div>
              <div className="text-[#64748B] mb-1">Importantes</div>
              <div className="text-[#0F172A]">2 requisitos</div>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-sm p-6">
          <h3 className="text-[#0F172A] mb-4">Status</h3>
          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <span className="text-[#64748B]">Documentados</span>
              <span className="text-[#10B981]">100%</span>
            </div>
            <div className="w-full bg-[#F1F5F9] rounded-full h-2">
              <div className="bg-[#10B981] h-2 rounded-full" style={{width: '100%'}}></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
```

---

## 📁 /components/BusinessRules.tsx

```tsx
import { AlertCircle, CheckCircle2, XCircle, Info } from 'lucide-react';

interface BusinessRule {
  id: string;
  name: string;
  description: string;
  type: 'Validação' | 'Cálculo' | 'Autorização' | 'Processamento';
  impact: 'Crítico' | 'Alto' | 'Médio';
  example?: string;
}

export function BusinessRules() {
  const rules: BusinessRule[] = [
    {
      id: 'RN001',
      name: 'Critérios de Avaliação',
      description: 'A pontuação final de uma ligação é calculada pela média ponderada de 5 critérios: Clareza (peso 20%), Cortesia (20%), Objetividade (20%), Resolução do Problema (30%), Tempo de Atendimento (10%).',
      type: 'Cálculo',
      impact: 'Crítico',
      example: 'Pontuação = (Clareza×0.2) + (Cortesia×0.2) + (Objetividade×0.2) + (Resolução×0.3) + (Tempo×0.1)'
    },
    {
      id: 'RN002',
      name: 'Tamanho Máximo de Arquivo',
      description: 'Arquivos de áudio não podem exceder 50MB. Ligações com mais de 30 minutos devem ser fragmentadas.',
      type: 'Validação',
      impact: 'Alto',
      example: 'Limite: 50MB ou 30 minutos de áudio'
    },
    {
      id: 'RN003',
      name: 'Nível de Acesso',
      description: 'Apenas supervisores podem visualizar dados de todos os atendentes. Atendentes só podem acessar suas próprias avaliações.',
      type: 'Autorização',
      impact: 'Crítico',
      example: 'Atendente: ver apenas próprios dados | Supervisor: ver todos'
    },
    {
      id: 'RN004',
      name: 'Threshold de Alerta',
      description: 'Se a pontuação de uma ligação for inferior a 60%, o sistema deve gerar um alerta automático para o supervisor.',
      type: 'Processamento',
      impact: 'Alto',
      example: 'Score < 60% → Alerta automático'
    },
    {
      id: 'RN005',
      name: 'Retenção de Dados',
      description: 'Ligações e avaliações devem ser mantidas no sistema por no mínimo 12 meses para fins de auditoria.',
      type: 'Processamento',
      impact: 'Médio',
      example: 'Período mínimo: 12 meses'
    },
    {
      id: 'RN006',
      name: 'Tentativas de Login',
      description: 'Após 5 tentativas incorretas de login, a conta do usuário deve ser bloqueada temporariamente por 15 minutos.',
      type: 'Validação',
      impact: 'Alto',
      example: '5 falhas → Bloqueio de 15 minutos'
    },
    {
      id: 'RN007',
      name: 'Formato de Pontuação',
      description: 'Todas as pontuações devem ser exibidas em escala de 0 a 100, com duas casas decimais.',
      type: 'Cálculo',
      impact: 'Médio',
      example: 'Formato: 85.75 (0-100)'
    },
    {
      id: 'RN008',
      name: 'Idioma de Análise',
      description: 'O sistema deve suportar análise de ligações em português brasileiro. Ligações em outros idiomas devem ser rejeitadas.',
      type: 'Validação',
      impact: 'Crítico',
      example: 'Idioma suportado: PT-BR'
    }
  ];

  const getTypeColor = (type: string) => {
    const colors: Record<string, string> = {
      'Validação': 'bg-[#EF4444]',
      'Cálculo': 'bg-[#4F46E5]',
      'Autorização': 'bg-[#F59E0B]',
      'Processamento': 'bg-[#10B981]'
    };
    return colors[type] || 'bg-[#64748B]';
  };

  const getImpactIcon = (impact: string) => {
    if (impact === 'Crítico') return <AlertCircle className="w-5 h-5 text-[#EF4444]" />;
    if (impact === 'Alto') return <Info className="w-5 h-5 text-[#F59E0B]" />;
    return <CheckCircle2 className="w-5 h-5 text-[#10B981]" />;
  };

  const getImpactColor = (impact: string) => {
    if (impact === 'Crítico') return 'text-[#EF4444]';
    if (impact === 'Alto') return 'text-[#F59E0B]';
    return 'text-[#10B981]';
  };

  return (
    <div className="p-8">
      <div className="mb-8">
        <h1 className="text-[#0F172A] mb-2">Regras de Negócio</h1>
        <p className="text-[#64748B]">
          Políticas e lógicas que governam o funcionamento do sistema
        </p>
      </div>

      <div className="grid grid-cols-4 gap-4 mb-6">
        {['Validação', 'Cálculo', 'Autorização', 'Processamento'].map(type => {
          const count = rules.filter(r => r.type === type).length;
          return (
            <div key={type} className="bg-white rounded-lg shadow-sm p-4">
              <div className={`w-10 h-10 ${getTypeColor(type)} rounded-lg flex items-center justify-center text-white mb-3`}>
                {count}
              </div>
              <div className="text-[#0F172A] mb-1">{type}</div>
              <div className="text-xs text-[#64748B]">{count} regras</div>
            </div>
          );
        })}
      </div>

      <div className="space-y-4 mb-6">
        {rules.map((rule) => (
          <div 
            key={rule.id}
            className="bg-white rounded-lg shadow-sm p-6 border border-[#E2E8F0] hover:border-[#4F46E5] transition-colors"
          >
            <div className="flex items-start justify-between mb-3">
              <div className="flex items-center gap-3">
                <span className="text-[#4F46E5]">{rule.id}</span>
                <h3 className="text-[#0F172A]">{rule.name}</h3>
              </div>
              <div className="flex items-center gap-2">
                {getImpactIcon(rule.impact)}
                <span className={`${getImpactColor(rule.impact)}`}>
                  {rule.impact}
                </span>
              </div>
            </div>

            <p className="text-[#0F172A] mb-3">
              {rule.description}
            </p>

            <div className="flex items-center gap-3">
              <span className={`px-3 py-1 ${getTypeColor(rule.type)} text-white rounded-full text-xs`}>
                {rule.type}
              </span>
              
              {rule.example && (
                <div className="px-3 py-1 bg-[#F8FAFC] rounded text-xs text-[#64748B] font-mono">
                  {rule.example}
                </div>
              )}
            </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-2 gap-6">
        <div className="bg-[#FEF2F2] border border-[#EF4444] rounded-lg p-6">
          <div className="flex items-start gap-3">
            <AlertCircle className="w-6 h-6 text-[#EF4444] flex-shrink-0 mt-1" />
            <div>
              <h3 className="text-[#EF4444] mb-2">Regras Críticas</h3>
              <p className="text-[#0F172A]">
                {rules.filter(r => r.impact === 'Crítico').length} regras classificadas como críticas 
                requerem atenção especial durante a implementação e testes.
              </p>
            </div>
          </div>
        </div>

        <div className="bg-[#EFF6FF] border border-[#4F46E5] rounded-lg p-6">
          <div className="flex items-start gap-3">
            <CheckCircle2 className="w-6 h-6 text-[#4F46E5] flex-shrink-0 mt-1" />
            <div>
              <h3 className="text-[#4F46E5] mb-2">Validação Implementada</h3>
              <p className="text-[#0F172A]">
                Todas as regras de negócio devem ser validadas tanto no frontend quanto no backend 
                para garantir integridade dos dados.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
```

---

## 📁 /components/DatabaseArchitecture.tsx

```tsx
import { Database, Key, Link2, FileText } from 'lucide-react';

interface Table {
  name: string;
  displayName: string;
  fields: {
    name: string;
    type: string;
    constraint: string;
  }[];
  color: string;
}

interface Relationship {
  from: string;
  to: string;
  type: string;
}

export function DatabaseArchitecture() {
  const tables: Table[] = [
    {
      name: 'tb_usuario',
      displayName: 'Usuário',
      color: 'bg-[#4F46E5]',
      fields: [
        { name: 'id', type: 'BIGINT', constraint: 'PK' },
        { name: 'nome', type: 'VARCHAR(100)', constraint: 'NOT NULL' },
        { name: 'email', type: 'VARCHAR(100)', constraint: 'UNIQUE' },
        { name: 'senha', type: 'VARCHAR(255)', constraint: 'NOT NULL' },
        { name: 'tipo_usuario', type: 'ENUM', constraint: 'NOT NULL' },
        { name: 'ativo', type: 'BOOLEAN', constraint: 'DEFAULT TRUE' }
      ]
    },
    {
      name: 'tb_ligacao',
      displayName: 'Ligação',
      color: 'bg-[#10B981]',
      fields: [
        { name: 'id', type: 'BIGINT', constraint: 'PK' },
        { name: 'usuario_id', type: 'BIGINT', constraint: 'FK' },
        { name: 'arquivo_audio', type: 'VARCHAR(255)', constraint: 'NOT NULL' },
        { name: 'duracao', type: 'INT', constraint: '' },
        { name: 'data_upload', type: 'TIMESTAMP', constraint: 'DEFAULT NOW()' },
        { name: 'status', type: 'ENUM', constraint: 'NOT NULL' }
      ]
    },
    {
      name: 'tb_avaliacao',
      displayName: 'Avaliação',
      color: 'bg-[#F59E0B]',
      fields: [
        { name: 'id', type: 'BIGINT', constraint: 'PK' },
        { name: 'ligacao_id', type: 'BIGINT', constraint: 'FK' },
        { name: 'pontuacao_final', type: 'DECIMAL(5,2)', constraint: 'NOT NULL' },
        { name: 'clareza', type: 'DECIMAL(5,2)', constraint: '' },
        { name: 'cortesia', type: 'DECIMAL(5,2)', constraint: '' },
        { name: 'objetividade', type: 'DECIMAL(5,2)', constraint: '' },
        { name: 'data_avaliacao', type: 'TIMESTAMP', constraint: 'DEFAULT NOW()' }
      ]
    },
    {
      name: 'tb_criterio',
      displayName: 'Critério',
      color: 'bg-[#8B5CF6]',
      fields: [
        { name: 'id', type: 'BIGINT', constraint: 'PK' },
        { name: 'nome', type: 'VARCHAR(50)', constraint: 'UNIQUE' },
        { name: 'descricao', type: 'TEXT', constraint: '' },
        { name: 'peso', type: 'DECIMAL(3,2)', constraint: 'NOT NULL' },
        { name: 'ativo', type: 'BOOLEAN', constraint: 'DEFAULT TRUE' }
      ]
    },
    {
      name: 'tb_relatorio',
      displayName: 'Relatório',
      color: 'bg-[#EC4899]',
      fields: [
        { name: 'id', type: 'BIGINT', constraint: 'PK' },
        { name: 'usuario_id', type: 'BIGINT', constraint: 'FK' },
        { name: 'tipo_relatorio', type: 'ENUM', constraint: 'NOT NULL' },
        { name: 'periodo_inicio', type: 'DATE', constraint: '' },
        { name: 'periodo_fim', type: 'DATE', constraint: '' },
        { name: 'data_geracao', type: 'TIMESTAMP', constraint: 'DEFAULT NOW()' }
      ]
    },
    {
      name: 'tb_log_auditoria',
      displayName: 'Log de Auditoria',
      color: 'bg-[#06B6D4]',
      fields: [
        { name: 'id', type: 'BIGINT', constraint: 'PK' },
        { name: 'usuario_id', type: 'BIGINT', constraint: 'FK' },
        { name: 'acao', type: 'VARCHAR(100)', constraint: 'NOT NULL' },
        { name: 'tabela_afetada', type: 'VARCHAR(50)', constraint: '' },
        { name: 'registro_id', type: 'BIGINT', constraint: '' },
        { name: 'timestamp', type: 'TIMESTAMP', constraint: 'DEFAULT NOW()' }
      ]
    }
  ];

  const relationships: Relationship[] = [
    { from: 'tb_ligacao', to: 'tb_usuario', type: '1:N' },
    { from: 'tb_avaliacao', to: 'tb_ligacao', type: '1:1' },
    { from: 'tb_relatorio', to: 'tb_usuario', type: '1:N' },
    { from: 'tb_log_auditoria', to: 'tb_usuario', type: '1:N' }
  ];

  return (
    <div className="p-8">
      <div className="mb-8">
        <h1 className="text-[#0F172A] mb-2">Arquitetura de Banco de Dados</h1>
        <p className="text-[#64748B]">
          Modelo relacional e estrutura das tabelas do sistema
        </p>
      </div>

      <div className="grid grid-cols-3 gap-4 mb-6">
        <div className="bg-white rounded-lg shadow-sm p-4">
          <div className="flex items-center gap-3 mb-2">
            <Database className="w-5 h-5 text-[#4F46E5]" />
            <span className="text-[#64748B]">Total de Tabelas</span>
          </div>
          <div className="text-[#0F172A]">6 tabelas</div>
        </div>

        <div className="bg-white rounded-lg shadow-sm p-4">
          <div className="flex items-center gap-3 mb-2">
            <Link2 className="w-5 h-5 text-[#10B981]" />
            <span className="text-[#64748B]">Relacionamentos</span>
          </div>
          <div className="text-[#0F172A]">{relationships.length} relações</div>
        </div>

        <div className="bg-white rounded-lg shadow-sm p-4">
          <div className="flex items-center gap-3 mb-2">
            <FileText className="w-5 h-5 text-[#F59E0B]" />
            <span className="text-[#64748B]">SGBD</span>
          </div>
          <div className="text-[#0F172A]">MySQL 8.0</div>
        </div>
      </div>

      {/* Entity Relationship Diagram */}
      <div className="bg-white rounded-lg shadow-sm p-8 mb-6">
        <h2 className="text-[#0F172A] mb-6">Diagrama Entidade-Relacionamento</h2>
        
        <div className="grid grid-cols-3 gap-6">
          {tables.map((table) => (
            <div 
              key={table.name}
              className="border-2 border-[#E2E8F0] rounded-lg overflow-hidden hover:border-[#4F46E5] transition-colors"
            >
              <div className={`${table.color} text-white p-4`}>
                <div className="flex items-center gap-2">
                  <Database className="w-5 h-5" />
                  <div>
                    <div className="font-mono text-xs opacity-80">{table.name}</div>
                    <div>{table.displayName}</div>
                  </div>
                </div>
              </div>
              
              <div className="p-4 bg-white">
                <div className="space-y-2">
                  {table.fields.map((field) => (
                    <div 
                      key={field.name}
                      className="flex items-center gap-2 text-sm"
                    >
                      {field.constraint === 'PK' && (
                        <Key className="w-3 h-3 text-[#F59E0B] flex-shrink-0" />
                      )}
                      {field.constraint === 'FK' && (
                        <Link2 className="w-3 h-3 text-[#4F46E5] flex-shrink-0" />
                      )}
                      {!field.constraint.includes('K') && (
                        <div className="w-3 h-3 flex-shrink-0" />
                      )}
                      
                      <div className="flex-1">
                        <span className="text-[#0F172A]">{field.name}</span>
                        <span className="text-[#64748B] text-xs ml-2">{field.type}</span>
                      </div>
                      
                      {field.constraint && (
                        <span className="text-xs text-[#64748B] px-2 py-0.5 bg-[#F8FAFC] rounded">
                          {field.constraint}
                        </span>
                      )}
                    </div>
                  ))}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Relationships */}
      <div className="bg-white rounded-lg shadow-sm p-6 mb-6">
        <h2 className="text-[#0F172A] mb-4">Relacionamentos</h2>
        <div className="space-y-3">
          {relationships.map((rel, idx) => {
            const fromTable = tables.find(t => t.name === rel.from);
            const toTable = tables.find(t => t.name === rel.to);
            
            return (
              <div 
                key={idx}
                className="flex items-center gap-4 p-4 bg-[#F8FAFC] rounded-lg"
              >
                <div className={`px-4 py-2 ${fromTable?.color} text-white rounded text-sm`}>
                  {fromTable?.displayName}
                </div>
                
                <div className="flex items-center gap-2 text-[#64748B]">
                  <div className="h-px w-8 bg-[#E2E8F0]"></div>
                  <span className="text-xs bg-white px-2 py-1 rounded border border-[#E2E8F0]">
                    {rel.type}
                  </span>
                  <div className="h-px w-8 bg-[#E2E8F0]"></div>
                </div>
                
                <div className={`px-4 py-2 ${toTable?.color} text-white rounded text-sm`}>
                  {toTable?.displayName}
                </div>
                
                <div className="ml-auto text-sm text-[#64748B]">
                  {rel.from} → {rel.to}
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* Legend */}
      <div className="grid grid-cols-2 gap-6">
        <div className="bg-[#F8FAFC] rounded-lg p-6">
          <h3 className="text-[#0F172A] mb-4">Legenda</h3>
          <div className="space-y-3">
            <div className="flex items-center gap-3">
              <Key className="w-4 h-4 text-[#F59E0B]" />
              <span className="text-[#0F172A]">PK - Primary Key (Chave Primária)</span>
            </div>
            <div className="flex items-center gap-3">
              <Link2 className="w-4 h-4 text-[#4F46E5]" />
              <span className="text-[#0F172A]">FK - Foreign Key (Chave Estrangeira)</span>
            </div>
            <div className="flex items-center gap-3">
              <Database className="w-4 h-4 text-[#64748B]" />
              <span className="text-[#0F172A]">Tabela do Sistema</span>
            </div>
          </div>
        </div>

        <div className="bg-[#EEF2FF] border border-[#4F46E5] rounded-lg p-6">
          <h3 className="text-[#4F46E5] mb-3">Observações Técnicas</h3>
          <ul className="space-y-2 text-[#0F172A]">
            <li className="flex items-start gap-2">
              <span className="text-[#4F46E5] mt-1">•</span>
              <span>Todas as tabelas usam BIGINT para IDs</span>
            </li>
            <li className="flex items-start gap-2">
              <span className="text-[#4F46E5] mt-1">•</span>
              <span>Timestamps automáticos para auditoria</span>
            </li>
            <li className="flex items-start gap-2">
              <span className="text-[#4F46E5] mt-1">•</span>
              <span>Índices criados em todas as FKs</span>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
}
```

---

## 📦 INSTRUÇÕES DE INSTALAÇÃO

### Dependências necessárias:
```json
{
  "dependencies": {
    "react": "^18.2.0",
    "lucide-react": "latest"
  }
}
```

### Como usar:

1. **Copie cada arquivo** para sua estrutura de pastas correspondente
2. **Instale as dependências**: `npm install lucide-react`
3. **Execute o projeto**: O app já está pronto para rodar!

### Estrutura de pastas:
```
/
├── App.tsx
├── components/
│   ├── Sidebar.tsx
│   ├── OverviewSection.tsx
│   ├── FunctionalRequirements.tsx
│   ├── NonFunctionalRequirements.tsx
│   ├── BusinessRules.tsx
│   └── DatabaseArchitecture.tsx
```

### Design System utilizado:
- **Primary**: #4F46E5 (Indigo)
- **Neutral 900**: #0F172A (Texto principal)
- **Neutral 500**: #64748B (Texto secundário)
- **Surface 100**: #F1F5F9 (Fundo)
- **Surface White**: #FFFFFF (Cards)
- **Sucesso**: #10B981
- **Alerta**: #F59E0B
- **Erro**: #EF4444

---

## ✨ FUNCIONALIDADES

✅ Navegação entre 5 seções
✅ Filtro de requisitos por ator
✅ Tabelas interativas
✅ Diagrama ER visual
✅ Design responsivo
✅ Animações suaves
✅ Sistema de cores profissional

---

**Desenvolvido para CallQuality AI - Portal de Documentação Técnica v1.0.0**
