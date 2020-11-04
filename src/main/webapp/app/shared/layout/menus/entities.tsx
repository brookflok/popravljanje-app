import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <MenuItem icon="asterisk" to="/dodatni-info-user">
      <Translate contentKey="global.menu.entities.dodatniInfoUser" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/artikl">
      <Translate contentKey="global.menu.entities.artikl" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/usluga">
      <Translate contentKey="global.menu.entities.usluga" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/potreba">
      <Translate contentKey="global.menu.entities.potreba" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/galerija">
      <Translate contentKey="global.menu.entities.galerija" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/main-slika">
      <Translate contentKey="global.menu.entities.mainSlika" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/profilna-slika">
      <Translate contentKey="global.menu.entities.profilnaSlika" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/slika">
      <Translate contentKey="global.menu.entities.slika" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/informacije">
      <Translate contentKey="global.menu.entities.informacije" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/javno-pitanje">
      <Translate contentKey="global.menu.entities.javnoPitanje" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/odgovor-na-javno-pitanje">
      <Translate contentKey="global.menu.entities.odgovorNaJavnoPitanje" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entiteti">
      <Translate contentKey="global.menu.entities.entiteti" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/kanton">
      <Translate contentKey="global.menu.entities.kanton" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/lokacija">
      <Translate contentKey="global.menu.entities.lokacija" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/ucesnici">
      <Translate contentKey="global.menu.entities.ucesnici" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/poruka">
      <Translate contentKey="global.menu.entities.poruka" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/chat">
      <Translate contentKey="global.menu.entities.chat" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
